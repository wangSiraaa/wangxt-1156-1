package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReshootCreateDTO {

    public ReshootCreateDTO() {
    }

    @NotNull(message = "缺陷ID不能为空")
    private Long defectId;

    private String reshootReason;

    private LocalDateTime scheduledTime;

    private BigDecimal windSpeedScheduled;

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public String getReshootReason() {
        return reshootReason;
    }

    public void setReshootReason(String reshootReason) {
        this.reshootReason = reshootReason;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public BigDecimal getWindSpeedScheduled() {
        return windSpeedScheduled;
    }

    public void setWindSpeedScheduled(BigDecimal windSpeedScheduled) {
        this.windSpeedScheduled = windSpeedScheduled;
    }
}
