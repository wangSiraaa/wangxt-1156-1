package com.windfarm.defect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaintenanceWindowCreateDTO {

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
}
