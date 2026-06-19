package com.windfarm.defect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceWindowUpdateDTO {

    public MaintenanceWindowUpdateDTO() {
    }

    private String workOrderCode;

    private String workOrderUrl;

    private String maintenancePhotos;

    private String reviewConclusion;

    private String reviewOpinion;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private BigDecimal actualWindSpeed;

    private String remark;

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public String getWorkOrderUrl() {
        return workOrderUrl;
    }

    public void setWorkOrderUrl(String workOrderUrl) {
        this.workOrderUrl = workOrderUrl;
    }

    public String getMaintenancePhotos() {
        return maintenancePhotos;
    }

    public void setMaintenancePhotos(String maintenancePhotos) {
        this.maintenancePhotos = maintenancePhotos;
    }

    public String getReviewConclusion() {
        return reviewConclusion;
    }

    public void setReviewConclusion(String reviewConclusion) {
        this.reviewConclusion = reviewConclusion;
    }

    public String getReviewOpinion() {
        return reviewOpinion;
    }

    public void setReviewOpinion(String reviewOpinion) {
        this.reviewOpinion = reviewOpinion;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public BigDecimal getActualWindSpeed() {
        return actualWindSpeed;
    }

    public void setActualWindSpeed(BigDecimal actualWindSpeed) {
        this.actualWindSpeed = actualWindSpeed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
