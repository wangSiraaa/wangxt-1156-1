package com.windfarm.defect.dto;

import com.windfarm.defect.enums.ReviewConclusion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DefectReviewDTO {

    public DefectReviewDTO() {
    }

    @NotNull(message = "复核结论不能为空")
    private ReviewConclusion reviewConclusion;

    private String reviewOpinion;

    private Boolean needOutage;

    private LocalDateTime plannedOutageStartTime;

    private LocalDateTime plannedOutageEndTime;

    public ReviewConclusion getReviewConclusion() {
        return reviewConclusion;
    }

    public void setReviewConclusion(ReviewConclusion reviewConclusion) {
        this.reviewConclusion = reviewConclusion;
    }

    public String getReviewOpinion() {
        return reviewOpinion;
    }

    public void setReviewOpinion(String reviewOpinion) {
        this.reviewOpinion = reviewOpinion;
    }

    public Boolean getNeedOutage() {
        return needOutage;
    }

    public void setNeedOutage(Boolean needOutage) {
        this.needOutage = needOutage;
    }

    public LocalDateTime getPlannedOutageStartTime() {
        return plannedOutageStartTime;
    }

    public void setPlannedOutageStartTime(LocalDateTime plannedOutageStartTime) {
        this.plannedOutageStartTime = plannedOutageStartTime;
    }

    public LocalDateTime getPlannedOutageEndTime() {
        return plannedOutageEndTime;
    }

    public void setPlannedOutageEndTime(LocalDateTime plannedOutageEndTime) {
        this.plannedOutageEndTime = plannedOutageEndTime;
    }
}
