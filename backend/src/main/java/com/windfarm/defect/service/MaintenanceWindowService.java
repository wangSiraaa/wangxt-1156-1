package com.windfarm.defect.service;

import com.windfarm.defect.dto.MaintenanceWindowCreateDTO;
import com.windfarm.defect.dto.MaintenanceWindowUpdateDTO;
import com.windfarm.defect.entity.MaintenanceWindow;
import com.windfarm.defect.entity.OutageRecord;
import com.windfarm.defect.enums.TurbineStatus;
import com.windfarm.defect.enums.WindowStatus;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.MaintenanceWindowRepository;
import com.windfarm.defect.repository.OutageRecordRepository;
import com.windfarm.defect.repository.WindTurbineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MaintenanceWindowService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MaintenanceWindowService.class);

    @Autowired
    private MaintenanceWindowRepository maintenanceWindowRepository;
    @Autowired
    private WindTurbineRepository windTurbineRepository;
    @Autowired
    private OutageRecordRepository outageRecordRepository;
    @Autowired
    private OutageService outageService;

    @Value("${windfarm.defect.wind-speed-threshold:12.0}")
    private double windSpeedThreshold;

    private static final AtomicInteger windowCounter = new AtomicInteger(1000);

    @Transactional
    public MaintenanceWindow createWindow(MaintenanceWindowCreateDTO dto, String operator) {
        validateTurbineExists(dto.getTurbineId());

        boolean isWindSpeedOverLimit = dto.getExpectedWindSpeed() != null
                && dto.getExpectedWindSpeed().doubleValue() > windSpeedThreshold;

        if (isWindSpeedOverLimit && !Boolean.TRUE.equals(dto.getIsReservation())) {
            throw new BusinessException("预计风速超过阈值(" + windSpeedThreshold
                    + "m/s)，不允许安排登塔检修。如需预约下一可用窗口，请设置isReservation=true");
        }

        MaintenanceWindow window = new MaintenanceWindow();
        window.setWindowCode(generateWindowCode());
        window.setDefectId(dto.getDefectId());
        window.setTurbineId(dto.getTurbineId());
        window.setPlannedStartTime(dto.getPlannedStartTime());
        window.setPlannedEndTime(dto.getPlannedEndTime());
        window.setExpectedWindSpeed(dto.getExpectedWindSpeed());
        window.setStatus(WindowStatus.PROPOSED);
        window.setWindowType(dto.getWindowType());
        window.setMaintenanceContent(dto.getMaintenanceContent());
        window.setMaintenanceTeam(dto.getMaintenanceTeam());
        window.setEvaluator(operator);
        window.setEvaluationTime(LocalDateTime.now());
        window.setEvaluationOpinion(dto.getEvaluationOpinion());
        window.setCreateBy(operator);
        window.setUpdateBy(operator);

        window.setIsReservation(Boolean.TRUE.equals(dto.getIsReservation()));
        if (Boolean.TRUE.equals(dto.getIsReservation())) {
            window.setReservationExpireTime(dto.getReservationExpireTime());
            log.info("创建预约检修窗口，机组ID: {}, 预计风速: {}m/s，预约到期时间: {}",
                    dto.getTurbineId(), dto.getExpectedWindSpeed(), dto.getReservationExpireTime());
        }

        MaintenanceWindow saved = maintenanceWindowRepository.save(window);
        log.info("检修窗口创建成功，窗口编号: {}, 机组ID: {}, 预约状态: {}",
                saved.getWindowCode(), dto.getTurbineId(), saved.getIsReservation());
        return saved;
    }

    public List<MaintenanceWindow> findAvailableWindows(Long turbineId) {
        List<MaintenanceWindow> windows = maintenanceWindowRepository.findAvailableWindows(
                turbineId, windSpeedThreshold, LocalDateTime.now());
        log.info("查询可用检修窗口，机组ID: {}, 可用窗口数: {}", turbineId, windows.size());
        return windows;
    }

    @Transactional
    public MaintenanceWindow confirmWindow(Long windowId, String operator) {
        MaintenanceWindow window = getWindowById(windowId);

        if (window.getStatus() != WindowStatus.PROPOSED) {
            throw new BusinessException("当前窗口状态不允许确认");
        }

        if (window.getExpectedWindSpeed() != null && window.getExpectedWindSpeed().doubleValue() > windSpeedThreshold) {
            throw new BusinessException("预计风速超过阈值(" + windSpeedThreshold + "m/s)，不能确认检修窗口");
        }

        window.setStatus(WindowStatus.CONFIRMED);
        window.setUpdateBy(operator);

        MaintenanceWindow saved = maintenanceWindowRepository.save(window);

        if (!outageService.isTurbineStopped(window.getTurbineId())) {
            outageService.createOutageFromWindow(windowId, operator);
        }

        log.info("检修窗口已确认，窗口ID: {}", windowId);
        return saved;
    }

    @Transactional
    public MaintenanceWindow startWindow(Long windowId, MaintenanceWindowUpdateDTO dto, String operator) {
        MaintenanceWindow window = getWindowById(windowId);

        if (window.getStatus() != WindowStatus.CONFIRMED) {
            throw new BusinessException("当前窗口状态不允许开始");
        }

        if (dto.getActualWindSpeed() != null && dto.getActualWindSpeed().doubleValue() > windSpeedThreshold) {
            throw new BusinessException("实际风速超过阈值(" + windSpeedThreshold + "m/s)，不能开始登塔检修");
        }

        window.setStatus(WindowStatus.IN_PROGRESS);
        window.setActualStartTime(dto.getActualStartTime() != null ? dto.getActualStartTime() : LocalDateTime.now());
        window.setActualWindSpeed(dto.getActualWindSpeed());
        window.setUpdateBy(operator);

        MaintenanceWindow saved = maintenanceWindowRepository.save(window);

        OutageRecord outage = outageRecordRepository.findActiveByTurbineId(window.getTurbineId())
                .orElse(null);
        if (outage != null) {
            outage.setActualStartTime(window.getActualStartTime());
            outageRecordRepository.save(outage);
        }

        log.info("检修窗口已开始，窗口ID: {}", windowId);
        return saved;
    }

    @Transactional
    public MaintenanceWindow completeWindow(Long windowId, MaintenanceWindowUpdateDTO dto, String operator) {
        MaintenanceWindow window = getWindowById(windowId);

        if (window.getStatus() != WindowStatus.IN_PROGRESS) {
            throw new BusinessException("当前窗口状态不允许完成");
        }

        window.setStatus(WindowStatus.COMPLETED);
        window.setActualEndTime(dto.getActualEndTime() != null ? dto.getActualEndTime() : LocalDateTime.now());
        window.setRemark(dto.getRemark());

        window.setWorkOrderCode(dto.getWorkOrderCode());
        window.setWorkOrderUrl(dto.getWorkOrderUrl());
        window.setMaintenancePhotos(dto.getMaintenancePhotos());
        window.setReviewConclusion(dto.getReviewConclusion());
        window.setReviewOpinion(dto.getReviewOpinion());
        window.setReviewer(operator);
        window.setReviewTime(LocalDateTime.now());

        window.setUpdateBy(operator);

        MaintenanceWindow saved = maintenanceWindowRepository.save(window);

        OutageRecord outage = outageRecordRepository.findActiveByTurbineId(window.getTurbineId())
                .orElse(null);
        if (outage != null && outage.getWindowId() != null && outage.getWindowId().equals(windowId)) {
            outage.setActualEndTime(window.getActualEndTime());
            outage.setIsActive(false);
            outageRecordRepository.save(outage);

            var turbine = windTurbineRepository.findById(window.getTurbineId()).orElse(null);
            if (turbine != null) {
                turbine.setStatus(TurbineStatus.STANDBY);
                windTurbineRepository.save(turbine);
            }
        }

        log.info("检修窗口已完成，窗口ID: {}, 工单编号: {}, 复核结论: {}",
                windowId, dto.getWorkOrderCode(), dto.getReviewConclusion());
        return saved;
    }

    @Transactional
    public MaintenanceWindow cancelWindow(Long windowId, String reason, String operator) {
        MaintenanceWindow window = getWindowById(windowId);

        if (window.getStatus() == WindowStatus.COMPLETED) {
            throw new BusinessException("已完成的检修窗口不能取消");
        }

        window.setStatus(WindowStatus.CANCELLED);
        window.setRemark(reason);
        window.setUpdateBy(operator);

        MaintenanceWindow saved = maintenanceWindowRepository.save(window);

        OutageRecord outage = outageRecordRepository.findActiveByTurbineId(window.getTurbineId())
                .orElse(null);
        if (outage != null && outage.getWindowId() != null && outage.getWindowId().equals(windowId)) {
            outage.setIsActive(false);
            outage.setRemark("检修窗口取消，停机记录自动结束");
            outageRecordRepository.save(outage);

            var turbine = windTurbineRepository.findById(window.getTurbineId()).orElse(null);
            if (turbine != null) {
                turbine.setStatus(TurbineStatus.STANDBY);
                windTurbineRepository.save(turbine);
            }
        }

        log.info("检修窗口已取消，窗口ID: {}", windowId);
        return saved;
    }

    public List<MaintenanceWindow> listWindows(Long turbineId, Long defectId, WindowStatus status) {
        if (turbineId != null && status != null) {
            return maintenanceWindowRepository.findByTurbineIdAndStatusIn(turbineId, List.of(status));
        } else if (turbineId != null) {
            return maintenanceWindowRepository.findByTurbineIdAndIsDeletedFalse(turbineId);
        } else if (defectId != null) {
            return maintenanceWindowRepository.findByDefectIdAndIsDeletedFalse(defectId);
        } else if (status != null) {
            return maintenanceWindowRepository.findByStatusAndIsDeletedFalse(status);
        }
        return maintenanceWindowRepository.findByIsDeletedFalse();
    }

    public MaintenanceWindow getWindowDetail(Long windowId) {
        return getWindowById(windowId);
    }

    private MaintenanceWindow getWindowById(Long windowId) {
        return maintenanceWindowRepository.findById(windowId)
                .filter(w -> !w.getIsDeleted())
                .orElseThrow(() -> new BusinessException("检修窗口不存在"));
    }

    private void validateTurbineExists(Long turbineId) {
        if (!windTurbineRepository.existsById(turbineId)) {
            throw new BusinessException("机组不存在");
        }
    }

    private String generateWindowCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = windowCounter.getAndIncrement();
        return "MW" + dateStr + String.format("%04d", seq);
    }

    public boolean canScheduleTowerClimb(double windSpeed) {
        return windSpeed <= windSpeedThreshold;
    }
}
