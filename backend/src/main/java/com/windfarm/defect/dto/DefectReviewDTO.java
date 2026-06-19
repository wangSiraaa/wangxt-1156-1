package com.windfarm.defect.dto;

import com.windfarm.defect.enums.ReviewConclusion;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DefectReviewDTO {

    @NotNull(message = "复核结论不能为空")
    private ReviewConclusion reviewConclusion;

    private String reviewOpinion;

    private Boolean needOutage;

    private LocalDateTime plannedOutageStartTime;

    private LocalDateTime plannedOutageEndTime;
}
