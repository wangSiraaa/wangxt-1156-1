package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GenerationPlanCreateDTO {

    @NotNull(message = "机组ID不能为空")
    private Long turbineId;

    private LocalDate planDate;

    private BigDecimal plannedOutput;

    private BigDecimal plannedHours;

    private String remark;
}
