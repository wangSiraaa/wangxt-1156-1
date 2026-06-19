package com.windfarm.defect.dto;

import lombok.Data;

@Data
public class DefectEvaluateDTO {

    private String evaluationOpinion;

    private String severityLevel;

    private Boolean needReshoot;

    private String reshootReason;
}
