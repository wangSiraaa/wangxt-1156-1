package com.windfarm.defect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReshootCompleteDTO {

    public ReshootCompleteDTO() {
    }

    private LocalDateTime actualTime;

    private BigDecimal windSpeedActual;

    private String photoUrls;

    private String reshootResult;

    private String remark;

    public LocalDateTime getActualTime() {
        return actualTime;
    }

    public void setActualTime(LocalDateTime actualTime) {
        this.actualTime = actualTime;
    }

    public BigDecimal getWindSpeedActual() {
        return windSpeedActual;
    }

    public void setWindSpeedActual(BigDecimal windSpeedActual) {
        this.windSpeedActual = windSpeedActual;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getReshootResult() {
        return reshootResult;
    }

    public void setReshootResult(String reshootResult) {
        this.reshootResult = reshootResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
