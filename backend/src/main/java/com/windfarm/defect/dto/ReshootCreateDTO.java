package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReshootCreateDTO {

    @NotNull(message = "缺陷ID不能为空")
    private Long defectId;

    private String reshootReason;

    private LocalDateTime scheduledTime;

    private BigDecimal windSpeedScheduled;
}
