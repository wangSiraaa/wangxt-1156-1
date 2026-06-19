package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GenerationPlanCreateDTO {

    public GenerationPlanCreateDTO() {
    }

    @NotNull(message = "机组ID不能为空")
    private Long turbineId;

    private LocalDate planDate;

    private BigDecimal plannedOutput;

    private BigDecimal plannedHours;

    private String remark;

    public Long getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(Long turbineId) {
        this.turbineId = turbineId;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public BigDecimal getPlannedOutput() {
        return plannedOutput;
    }

    public void setPlannedOutput(BigDecimal plannedOutput) {
        this.plannedOutput = plannedOutput;
    }

    public BigDecimal getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
