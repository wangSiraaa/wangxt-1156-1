package com.windfarm.defect.service;

import com.windfarm.defect.dto.GenerationPlanCreateDTO;
import com.windfarm.defect.entity.GenerationPlan;
import com.windfarm.defect.enums.TurbineStatus;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.GenerationPlanRepository;
import com.windfarm.defect.repository.OutageRecordRepository;
import com.windfarm.defect.repository.WindTurbineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationPlanService {

    private final GenerationPlanRepository generationPlanRepository;
    private final WindTurbineRepository windTurbineRepository;
    private final OutageRecordRepository outageRecordRepository;

    private static final AtomicInteger planCounter = new AtomicInteger(1);

    @Transactional
    public GenerationPlan createPlan(GenerationPlanCreateDTO dto, String operator) {
        validateTurbineExists(dto.getTurbineId());

        if (outageRecordRepository.existsActiveByTurbineId(dto.getTurbineId())) {
            throw new BusinessException("机组已确认停机，不能派发发电计划");
        }

        var turbine = windTurbineRepository.findById(dto.getTurbineId()).orElseThrow();
        if (turbine.getStatus() == TurbineStatus.STOPPED
                || turbine.getStatus() == TurbineStatus.MAINTENANCE
                || turbine.getStatus() == TurbineStatus.FAULT) {
            throw new BusinessException("机组状态为" + turbine.getStatus().getDescription()
                    + "，不能派发发电计划");
        }

        GenerationPlan plan = new GenerationPlan();
        plan.setPlanCode(generatePlanCode());
        plan.setTurbineId(dto.getTurbineId());
        plan.setPlanDate(dto.getPlanDate());
        plan.setPlannedOutput(dto.getPlannedOutput());
        plan.setPlannedHours(dto.getPlannedHours());
        plan.setStatus("PUBLISHED");
        plan.setRemark(dto.getRemark());
        plan.setCreateBy(operator);
        plan.setUpdateBy(operator);

        GenerationPlan saved = generationPlanRepository.save(plan);
        log.info("发电计划创建成功，计划编号: {}, 机组ID: {}", saved.getPlanCode(), dto.getTurbineId());
        return saved;
    }

    @Transactional
    public void cancelPlansForStoppedTurbine(Long turbineId, String operator) {
        List<GenerationPlan> activePlans = generationPlanRepository.findActivePlansByTurbineId(turbineId);
        for (GenerationPlan plan : activePlans) {
            plan.setStatus("CANCELLED");
            plan.setCancelReason("机组停机，发电计划自动取消");
            plan.setCancelTime(LocalDateTime.now());
            plan.setUpdateBy(operator);
            generationPlanRepository.save(plan);
            log.info("发电计划已取消，计划ID: {}, 原因: 机组停机", plan.getId());
        }
    }

    public List<GenerationPlan> listPlans(Long turbineId, String status) {
        if (turbineId != null) {
            return generationPlanRepository.findByTurbineIdAndIsDeletedFalseOrderByPlanDateDesc(turbineId);
        } else if (status != null) {
            return generationPlanRepository.findByStatusAndIsDeletedFalse(status);
        }
        return generationPlanRepository.findByIsDeletedFalse();
    }

    public GenerationPlan getPlanDetail(Long planId) {
        return generationPlanRepository.findById(planId)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new BusinessException("发电计划不存在"));
    }

    @Transactional
    public GenerationPlan cancelPlan(Long planId, String reason, String operator) {
        GenerationPlan plan = generationPlanRepository.findById(planId)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new BusinessException("发电计划不存在"));

        if ("CANCELLED".equals(plan.getStatus()) || "COMPLETED".equals(plan.getStatus())) {
            throw new BusinessException("当前计划状态不允许取消");
        }

        plan.setStatus("CANCELLED");
        plan.setCancelReason(reason);
        plan.setCancelTime(LocalDateTime.now());
        plan.setUpdateBy(operator);

        GenerationPlan saved = generationPlanRepository.save(plan);
        log.info("发电计划已取消，计划ID: {}", planId);
        return saved;
    }

    private void validateTurbineExists(Long turbineId) {
        if (!windTurbineRepository.existsById(turbineId)) {
            throw new BusinessException("机组不存在");
        }
    }

    private String generatePlanCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = planCounter.getAndIncrement();
        return "GP" + dateStr + String.format("%04d", seq);
    }
}
