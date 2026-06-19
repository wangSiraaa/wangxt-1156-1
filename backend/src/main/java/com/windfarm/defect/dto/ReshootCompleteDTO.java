package com.windfarm.defect.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReshootCompleteDTO {

    private LocalDateTime actualTime;

    private BigDecimal windSpeedActual;

    private String photoUrls;

    private String reshootResult;

    private String remark;
}
