package com.windfarm.defect.service;

import com.windfarm.defect.dto.ReshootCreateDTO;
import com.windfarm.defect.dto.ReshootCompleteDTO;
import com.windfarm.defect.entity.DefectRecord;
import com.windfarm.defect.entity.ReshootRecord;
import com.windfarm.defect.enums.DefectStatus;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.DefectRecordRepository;
import com.windfarm.defect.repository.ReshootRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReshootService {

    private final ReshootRecordRepository reshootRecordRepository;
    private final DefectRecordRepository defectRecordRepository;

    @Value("${windfarm.defect.wind-speed-threshold:12.0}")
    private double windSpeedThreshold;

    @Value("${windfarm.defect.max-reshoot-count:2}")
    private int maxReshootCount;

    private static final AtomicInteger reshootCounter = new AtomicInteger(1);

    @Transactional
    public ReshootRecord createReshoot(ReshootCreateDTO dto, String operator) {
        DefectRecord defect = defectRecordRepository.findById(dto.getDefectId())
                .filter(d -> !d.getIsDeleted())
                .orElseThrow(() -> new BusinessException("缺陷记录不存在"));

        if (defect.getStatus() != DefectStatus.PENDING_RESHOOT) {
            throw new BusinessException("当前缺陷状态不允许创建复拍");
        }

        int currentCount = reshootRecordRepository.countByDefectId(dto.getDefectId());
        if (currentCount >= maxReshootCount) {
            throw new BusinessException("已达到最大复拍次数(" + maxReshootCount + "次)，不允许再次复拍");
        }

        if (dto.getWindSpeedScheduled() != null && dto.getWindSpeedScheduled().doubleValue() > windSpeedThreshold) {
            throw new BusinessException("计划风速超过阈值(" + windSpeedThreshold + "m/s)，不允许安排复拍");
        }

        Integer maxOrder = reshootRecordRepository.findMaxReshootOrderByDefectId(dto.getDefectId());
        int nextOrder = (maxOrder == null ? 0 : maxOrder) + 1;

        ReshootRecord reshoot = new ReshootRecord();
        reshoot.setReshootCode(generateReshootCode());
        reshoot.setDefectId(dto.getDefectId());
        reshoot.setTurbineId(defect.getTurbineId());
        reshoot.setReshootReason(dto.getReshootReason());
        reshoot.setScheduledTime(dto.getScheduledTime());
        reshoot.setWindSpeedScheduled(dto.getWindSpeedScheduled());
        reshoot.setReshootOrder(nextOrder);
        reshoot.setIsCompleted(false);
        reshoot.setCreateBy(operator);
        reshoot.setUpdateBy(operator);

        ReshootRecord saved = reshootRecordRepository.save(reshoot);
        log.info("复拍任务创建成功，复拍编号: {}, 缺陷ID: {}", saved.getReshootCode(), dto.getDefectId());
        return saved;
    }

    @Transactional
    public ReshootRecord completeReshoot(Long reshootId, ReshootCompleteDTO dto, String operator) {
        ReshootRecord reshoot = reshootRecordRepository.findById(reshootId)
                .filter(r -> !r.getIsDeleted())
                .orElseThrow(() -> new BusinessException("复拍记录不存在"));

        if (reshoot.getIsCompleted()) {
            throw new BusinessException("该复拍任务已完成");
        }

        if (dto.getWindSpeedActual() != null && dto.getWindSpeedActual().doubleValue() > windSpeedThreshold) {
            throw new BusinessException("实际风速超过阈值(" + windSpeedThreshold + "m/s)，不允许执行复拍登塔作业");
        }

        reshoot.setActualTime(dto.getActualTime() != null ? dto.getActualTime() : LocalDateTime.now());
        reshoot.setWindSpeedActual(dto.getWindSpeedActual());
        reshoot.setPhotoUrls(dto.getPhotoUrls());
        reshoot.setReshootResult(dto.getReshootResult());
        reshoot.setReshootOperator(operator);
        reshoot.setIsCompleted(true);
        reshoot.setRemark(dto.getRemark());
        reshoot.setUpdateBy(operator);

        ReshootRecord saved = reshootRecordRepository.save(reshoot);

        updateDefectAfterReshoot(reshoot.getDefectId(), operator);

        log.info("复拍任务完成，复拍ID: {}, 缺陷ID: {}", reshootId, reshoot.getDefectId());
        return saved;
    }

    private void updateDefectAfterReshoot(Long defectId, String operator) {
        DefectRecord defect = defectRecordRepository.findById(defectId)
                .filter(d -> !d.getIsDeleted())
                .orElseThrow(() -> new BusinessException("缺陷记录不存在"));

        int completedCount = (int) reshootRecordRepository.findByDefectIdAndIsDeletedFalseOrderByReshootOrderAsc(defectId)
                .stream()
                .filter(ReshootRecord::getIsCompleted)
                .count();

        defect.setReshootCount(completedCount);

        if (completedCount >= maxReshootCount) {
            defect.setNeedReshoot(false);
            defect.setStatus(DefectStatus.PENDING_EVALUATION);
            log.info("缺陷已完成全部复拍次数，转入待评估状态，缺陷ID: {}", defectId);
        }

        defect.setUpdateBy(operator);
        defectRecordRepository.save(defect);
    }

    public List<ReshootRecord> listReshoots(Long defectId, Long turbineId) {
        if (defectId != null) {
            return reshootRecordRepository.findByDefectIdAndIsDeletedFalseOrderByReshootOrderAsc(defectId);
        } else if (turbineId != null) {
            return reshootRecordRepository.findByTurbineIdAndIsDeletedFalse(turbineId);
        }
        return reshootRecordRepository.findByIsCompletedFalseAndIsDeletedFalse();
    }

    public ReshootRecord getReshootDetail(Long reshootId) {
        return reshootRecordRepository.findById(reshootId)
                .filter(r -> !r.getIsDeleted())
                .orElseThrow(() -> new BusinessException("复拍记录不存在"));
    }

    @Transactional
    public void deleteReshoot(Long reshootId, String operator) {
        ReshootRecord reshoot = reshootRecordRepository.findById(reshootId)
                .filter(r -> !r.getIsDeleted())
                .orElseThrow(() -> new BusinessException("复拍记录不存在"));

        if (reshoot.getIsCompleted()) {
            throw new BusinessException("已完成的复拍记录不能删除");
        }

        reshoot.setIsDeleted(true);
        reshoot.setUpdateBy(operator);
        reshootRecordRepository.save(reshoot);
        log.info("复拍记录已删除，复拍ID: {}", reshootId);
    }

    private String generateReshootCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = reshootCounter.getAndIncrement();
        return "RS" + dateStr + String.format("%04d", seq);
    }
}
