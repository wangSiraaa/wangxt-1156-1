package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceWindowCreateDTO {

    public MaintenanceWindowCreateDTO() {
    }

    @NotNull(message = "机组ID不能为空")
    private Long turbineId;

    private Long defectId;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    private BigDecimal expectedWindSpeed;

    private String windowType;

    private String maintenanceContent;

    private String maintenanceTeam;

    private String evaluationOpinion;

    public Long getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(Long turbineId) {
        this.turbineId = turbineId;
    }

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalDateTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public BigDecimal getExpectedWindSpeed() {
        return expectedWindSpeed;
    }

    public void setExpectedWindSpeed(BigDecimal expectedWindSpeed) {
        this.expectedWindSpeed = expectedWindSpeed;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getMaintenanceTeam() {
        return maintenanceTeam;
    }

    public void setMaintenanceTeam(String maintenanceTeam) {
        this.maintenanceTeam = maintenanceTeam;
    }

    public String getEvaluationOpinion() {
        return evaluationOpinion;
    }

    public void setEvaluationOpinion(String evaluationOpinion) {
        this.evaluationOpinion = evaluationOpinion;
    }
}
