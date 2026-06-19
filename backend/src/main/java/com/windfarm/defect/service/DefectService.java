package com.windfarm.defect.service;

import com.windfarm.defect.dto.*;
import com.windfarm.defect.entity.DefectRecord;
import com.windfarm.defect.entity.ReshootRecord;
import com.windfarm.defect.enums.DefectStatus;
import com.windfarm.defect.enums.DefectType;
import com.windfarm.defect.enums.ReviewConclusion;
import com.windfarm.defect.enums.RoleType;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.DefectRecordRepository;
import com.windfarm.defect.repository.ReshootRecordRepository;
import com.windfarm.defect.repository.WindTurbineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class DefectService {

    private final DefectRecordRepository defectRecordRepository;
    private final ReshootRecordRepository reshootRecordRepository;
    private final WindTurbineRepository windTurbineRepository;
    private final OutageService outageService;

    @Value("${windfarm.defect.wind-speed-threshold:12.0}")
    private double windSpeedThreshold;

    @Value("${windfarm.defect.max-reshoot-count:2}")
    private int maxReshootCount;

    private static final AtomicInteger defectCounter = new AtomicInteger(1);

    @Transactional
    public DefectRecord uploadDefect(DefectUploadDTO dto, String operator) {
        validateTurbineExists(dto.getTurbineId());

        DefectRecord defect = new DefectRecord();
        BeanUtils.copyProperties(dto, defect);
        defect.setDefectCode(generateDefectCode());
        defect.setStatus(DefectStatus.PENDING_EVALUATION);
        defect.setInspectionTime(dto.getInspectionTime() != null ? dto.getInspectionTime() : LocalDateTime.now());
        defect.setInspector(operator);
        defect.setCreateBy(operator);
        defect.setUpdateBy(operator);
        defect.setReshootCount(0);
        defect.setNeedReshoot(false);

        if (isCrackType(dto.getDefectType())) {
            defect.setNeedReshoot(true);
            defect.setStatus(DefectStatus.PENDING_RESHOOT);
            log.info("缺陷类型为裂纹类，自动触发复拍流程，缺陷ID: {}", defect.getId());
        }

        DefectRecord saved = defectRecordRepository.save(defect);
        log.info("缺陷上传成功，缺陷编号: {}, 机组ID: {}", saved.getDefectCode(), saved.getTurbineId());
        return saved;
    }

    @Transactional
    public DefectRecord evaluateDefect(Long defectId, DefectEvaluateDTO dto, String evaluator) {
        DefectRecord defect = getDefectById(defectId);

        if (defect.getStatus() != DefectStatus.PENDING_EVALUATION
                && defect.getStatus() != DefectStatus.PENDING_RESHOOT) {
            throw new BusinessException("当前缺陷状态不允许评估");
        }

        defect.setEvaluationOpinion(dto.getEvaluationOpinion());
        defect.setEvaluator(evaluator);
        defect.setEvaluationTime(LocalDateTime.now());
        defect.setSeverityLevel(dto.getSeverityLevel());
        defect.setUpdateBy(evaluator);

        if (dto.getNeedReshoot() != null && dto.getNeedReshoot()) {
            validateReshootCount(defect);
            defect.setStatus(DefectStatus.PENDING_RESHOOT);
            defect.setNeedReshoot(true);
            log.info("缺陷评估后需要复拍，缺陷ID: {}", defectId);
        } else {
            defect.setStatus(DefectStatus.PENDING_MAINTENANCE);
            defect.setNeedReshoot(false);
            log.info("缺陷评估完成，进入待检修状态，缺陷ID: {}", defectId);
        }

        return defectRecordRepository.save(defect);
    }

    @Transactional
    public DefectRecord reviewDefect(Long defectId, DefectReviewDTO dto, String reviewer) {
        DefectRecord defect = getDefectById(defectId);

        if (defect.getStatus() != DefectStatus.PENDING_MAINTENANCE) {
            throw new BusinessException("当前缺陷状态不允许复核");
        }

        defect.setReviewConclusion(dto.getReviewConclusion());
        defect.setReviewOpinion(dto.getReviewOpinion());
        defect.setReviewer(reviewer);
        defect.setReviewTime(LocalDateTime.now());
        defect.setUpdateBy(reviewer);

        switch (dto.getReviewConclusion()) {
            case CONFIRMED_DEFECT, NEED_MAINTENANCE, NEED_IMMEDIATE_MAINTENANCE ->
                    defect.setStatus(DefectStatus.CONFIRMED);
            case FALSE_ALARM ->
                    defect.setStatus(DefectStatus.FALSE_ALARM);
            case NEED_OBSERVATION ->
                    defect.setStatus(DefectStatus.CLOSED);
        }

        if (dto.getNeedOutage() != null && dto.getNeedOutage()
                && (dto.getReviewConclusion() == ReviewConclusion.NEED_MAINTENANCE
                || dto.getReviewConclusion() == ReviewConclusion.NEED_IMMEDIATE_MAINTENANCE)) {
            outageService.createOutageFromDefect(defectId, dto, reviewer);
        }

        log.info("缺陷复核完成，结论: {}, 缺陷ID: {}", dto.getReviewConclusion(), defectId);
        return defectRecordRepository.save(defect);
    }

    public List<DefectRecord> listDefects(Long turbineId, DefectStatus status) {
        if (turbineId != null && status != null) {
            return defectRecordRepository.findByTurbineIdAndStatusAndIsDeletedFalse(turbineId, status);
        } else if (turbineId != null) {
            return defectRecordRepository.findByTurbineIdAndIsDeletedFalse(turbineId);
        } else if (status != null) {
            return defectRecordRepository.findByStatusAndIsDeletedFalse(status);
        }
        return defectRecordRepository.findByIsDeletedFalse();
    }

    public DefectRecord getDefectDetail(Long defectId) {
        return getDefectById(defectId);
    }

    @Transactional
    public void deleteDefect(Long defectId, String operator) {
        DefectRecord defect = getDefectById(defectId);
        defect.setIsDeleted(true);
        defect.setUpdateBy(operator);
        defectRecordRepository.save(defect);
        log.info("缺陷已删除，缺陷ID: {}", defectId);
    }

    private DefectRecord getDefectById(Long defectId) {
        return defectRecordRepository.findById(defectId)
                .filter(d -> !d.getIsDeleted())
                .orElseThrow(() -> new BusinessException("缺陷记录不存在"));
    }

    private void validateTurbineExists(Long turbineId) {
        if (!windTurbineRepository.existsById(turbineId)) {
            throw new BusinessException("机组不存在");
        }
    }

    private boolean isCrackType(DefectType defectType) {
        return defectType == DefectType.CRACK_SUSPECTED
                || defectType == DefectType.SURFACE_CRACK
                || defectType == DefectType.INTERNAL_CRACK;
    }

    private void validateReshootCount(DefectRecord defect) {
        int currentCount = reshootRecordRepository.countByDefectId(defect.getId());
        if (currentCount >= maxReshootCount) {
            throw new BusinessException("已达到最大复拍次数(" + maxReshootCount + "次)，不允许再次复拍");
        }
    }

    private String generateDefectCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = defectCounter.getAndIncrement();
        return "DEF" + dateStr + String.format("%04d", seq);
    }

    public boolean isWindSpeedOverThreshold(double windSpeed) {
        return windSpeed > windSpeedThreshold;
    }

    public double getWindSpeedThreshold() {
        return windSpeedThreshold;
    }
}
