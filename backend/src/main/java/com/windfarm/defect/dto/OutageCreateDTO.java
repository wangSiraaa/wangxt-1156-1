package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OutageCreateDTO {

    @NotNull(message = "机组ID不能为空")
    private Long turbineId;

    private Long defectId;

    private Long windowId;

    private String outageReason;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    private String remark;
}
