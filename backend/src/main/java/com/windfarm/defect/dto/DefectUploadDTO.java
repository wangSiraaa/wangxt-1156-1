package com.windfarm.defect.dto;

import com.windfarm.defect.enums.DefectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DefectUploadDTO {

    @NotNull(message = "机组ID不能为空")
    private Long turbineId;

    @NotNull(message = "叶片编号不能为空")
    private Integer bladeNumber;

    private String bladePosition;

    private String defectDescription;

    private DefectType defectType;

    private String severityLevel;

    private BigDecimal defectSize;

    private String sizeUnit;

    private String photoUrls;

    private LocalDateTime inspectionTime;

    private BigDecimal windSpeedInspection;
}
