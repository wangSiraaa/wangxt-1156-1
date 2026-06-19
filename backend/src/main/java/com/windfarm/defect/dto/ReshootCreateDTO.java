package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReshootCreateDTO {

    public ReshootCreateDTO() {
    }

    @NotNull(message = "缺陷ID不能为空")
    private Long defectId;

    private Integer bladeNumber;

    private String bladePosition;

    private BigDecimal requiredAngle;

    private BigDecimal requiredAzimuth;

    private BigDecimal angleTolerance;

    private String reshootReason;

    private LocalDateTime scheduledTime;

    private BigDecimal windSpeedScheduled;

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public Integer getBladeNumber() {
        return bladeNumber;
    }

    public void setBladeNumber(Integer bladeNumber) {
        this.bladeNumber = bladeNumber;
    }

    public String getBladePosition() {
        return bladePosition;
    }

    public void setBladePosition(String bladePosition) {
        this.bladePosition = bladePosition;
    }

    public BigDecimal getRequiredAngle() {
        return requiredAngle;
    }

    public void setRequiredAngle(BigDecimal requiredAngle) {
        this.requiredAngle = requiredAngle;
    }

    public BigDecimal getRequiredAzimuth() {
        return requiredAzimuth;
    }

    public void setRequiredAzimuth(BigDecimal requiredAzimuth) {
        this.requiredAzimuth = requiredAzimuth;
    }

    public BigDecimal getAngleTolerance() {
        return angleTolerance;
    }

    public void setAngleTolerance(BigDecimal angleTolerance) {
        this.angleTolerance = angleTolerance;
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
