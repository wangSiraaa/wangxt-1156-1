package com.windfarm.defect.dto;

import com.windfarm.defect.enums.DefectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DefectUploadDTO {

    public DefectUploadDTO() {
    }

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

    public Long getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(Long turbineId) {
        this.turbineId = turbineId;
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

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public BigDecimal getDefectSize() {
        return defectSize;
    }

    public void setDefectSize(BigDecimal defectSize) {
        this.defectSize = defectSize;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public LocalDateTime getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(LocalDateTime inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public BigDecimal getWindSpeedInspection() {
        return windSpeedInspection;
    }

    public void setWindSpeedInspection(BigDecimal windSpeedInspection) {
        this.windSpeedInspection = windSpeedInspection;
    }
}
