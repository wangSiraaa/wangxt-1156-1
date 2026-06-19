package com.windfarm.defect.service;

import com.windfarm.defect.dto.DefectReviewDTO;
import com.windfarm.defect.dto.OutageCreateDTO;
import com.windfarm.defect.entity.DefectRecord;
import com.windfarm.defect.entity.MaintenanceWindow;
import com.windfarm.defect.entity.OutageRecord;
import com.windfarm.defect.entity.WindTurbine;
import com.windfarm.defect.enums.TurbineStatus;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.DefectRecordRepository;
import com.windfarm.defect.repository.MaintenanceWindowRepository;
import com.windfarm.defect.repository.OutageRecordRepository;
import com.windfarm.defect.repository.WindTurbineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OutageService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OutageService.class);

    @Autowired
    private OutageRecordRepository outageRecordRepository;
    @Autowired
    private WindTurbineRepository windTurbineRepository;
    @Autowired
    private DefectRecordRepository defectRecordRepository;
    @Autowired
    private MaintenanceWindowRepository maintenanceWindowRepository;
    @Autowired
    private GenerationPlanService generationPlanService;

    private static final AtomicInteger outageCounter = new AtomicInteger(1000);

    @Transactional
    public OutageRecord createOutage(OutageCreateDTO dto, String operator) {
        validateTurbineExists(dto.getTurbineId());

        if (isTurbineStopped(dto.getTurbineId())) {
            throw new BusinessException("机组已处于停机状态，不能重复停机");
        }

        WindTurbine turbine = windTurbineRepository.findById(dto.getTurbineId()).orElseThrow();
        TurbineStatus statusBefore = turbine.getStatus();

        OutageRecord outage = new OutageRecord();
        outage.setOutageCode(generateOutageCode());
        outage.setTurbineId(dto.getTurbineId());
        outage.setDefectId(dto.getDefectId());
        outage.setWindowId(dto.getWindowId());
        outage.setTurbineStatusBefore(statusBefore);
        outage.setTurbineStatusAfter(TurbineStatus.STOPPED);
        outage.setOutageReason(dto.getOutageReason());
        outage.setPlannedStartTime(dto.getPlannedStartTime());
        outage.setPlannedEndTime(dto.getPlannedEndTime());
        outage.setIsActive(true);
        outage.setOperator(operator);
        outage.setApprover(operator);
        outage.setApprovalTime(LocalDateTime.now());
        outage.setRemark(dto.getRemark());
        outage.setCreateBy(operator);
        outage.setUpdateBy(operator);

        OutageRecord saved = outageRecordRepository.save(outage);

        turbine.setStatus(TurbineStatus.STOPPED);
        windTurbineRepository.save(turbine);

        generationPlanService.cancelPlansForStoppedTurbine(dto.getTurbineId(), operator);

        log.info("机组停机成功，停机编号: {}, 机组ID: {}", saved.getOutageCode(), dto.getTurbineId());
        return saved;
    }

    @Transactional
    public OutageRecord createOutageFromDefect(Long defectId, DefectReviewDTO dto, String operator) {
        DefectRecord defect = defectRecordRepository.findById(defectId)
                .filter(d -> !d.getIsDeleted())
                .orElseThrow(() -> new BusinessException("缺陷记录不存在"));

        OutageCreateDTO outageDTO = new OutageCreateDTO();
        outageDTO.setTurbineId(defect.getTurbineId());
        outageDTO.setDefectId(defectId);
        outageDTO.setOutageReason("缺陷复核确认需停机检修: " + dto.getReviewOpinion());
        outageDTO.setPlannedStartTime(dto.getPlannedOutageStartTime());
        outageDTO.setPlannedEndTime(dto.getPlannedOutageEndTime());
        outageDTO.setRemark("缺陷ID: " + defectId);

        return createOutage(outageDTO, operator);
    }

    @Transactional
    public OutageRecord createOutageFromWindow(Long windowId, String operator) {
        MaintenanceWindow window = maintenanceWindowRepository.findById(windowId)
                .filter(w -> !w.getIsDeleted())
                .orElseThrow(() -> new BusinessException("检修窗口不存在"));

        OutageCreateDTO outageDTO = new OutageCreateDTO();
        outageDTO.setTurbineId(window.getTurbineId());
        outageDTO.setDefectId(window.getDefectId());
        outageDTO.setWindowId(windowId);
        outageDTO.setOutageReason("检修窗口确认停机: " + window.getMaintenanceContent());
        outageDTO.setPlannedStartTime(window.getPlannedStartTime());
        outageDTO.setPlannedEndTime(window.getPlannedEndTime());
        outageDTO.setRemark("检修窗口ID: " + windowId);

        return createOutage(outageDTO, operator);
    }

    @Transactional
    public OutageRecord endOutage(Long outageId, String operator) {
        OutageRecord outage = outageRecordRepository.findById(outageId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new BusinessException("停机记录不存在"));

        if (!outage.getIsActive()) {
            throw new BusinessException("该停机记录已结束");
        }

        outage.setIsActive(false);
        outage.setActualEndTime(LocalDateTime.now());
        outage.setUpdateBy(operator);

        OutageRecord saved = outageRecordRepository.save(outage);

        WindTurbine turbine = windTurbineRepository.findById(outage.getTurbineId()).orElse(null);
        if (turbine != null) {
            turbine.setStatus(TurbineStatus.STANDBY);
            windTurbineRepository.save(turbine);
            log.info("机组已恢复备用状态，机组ID: {}", outage.getTurbineId());
        }

        log.info("停机记录已结束，停机ID: {}", outageId);
        return saved;
    }

    public boolean isTurbineStopped(Long turbineId) {
        return outageRecordRepository.existsActiveByTurbineId(turbineId);
    }

    public List<OutageRecord> listOutages(Long turbineId, Boolean isActive) {
        if (turbineId != null) {
            return outageRecordRepository.findByTurbineIdAndIsDeletedFalseOrderByCreateTimeDesc(turbineId);
        } else if (isActive != null && isActive) {
            return outageRecordRepository.findByIsActiveTrueAndIsDeletedFalse();
        } else if (isActive != null && !isActive) {
            return outageRecordRepository.findByIsActiveFalseAndIsDeletedFalse();
        }
        return outageRecordRepository.findByIsDeletedFalseOrderByCreateTimeDesc();
    }

    public OutageRecord getOutageDetail(Long outageId) {
        return outageRecordRepository.findById(outageId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new BusinessException("停机记录不存在"));
    }

    public OutageRecord getActiveOutageByTurbine(Long turbineId) {
        return outageRecordRepository.findActiveByTurbineId(turbineId)
                .orElse(null);
    }

    private void validateTurbineExists(Long turbineId) {
        if (!windTurbineRepository.existsById(turbineId)) {
            throw new BusinessException("机组不存在");
        }
    }

    private String generateOutageCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = outageCounter.getAndIncrement();
        return "OUT" + dateStr + String.format("%04d", seq);
    }
}
