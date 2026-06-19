package com.windfarm.defect.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaintenanceWindowUpdateDTO {

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private BigDecimal actualWindSpeed;

    private String remark;
}
