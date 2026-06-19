package com.windfarm.defect.dto;

public class DefectEvaluateDTO {

    public DefectEvaluateDTO() {
    }

    private String evaluationOpinion;

    private String severityLevel;

    private Boolean needReshoot;

    private String reshootReason;

    public String getEvaluationOpinion() {
        return evaluationOpinion;
    }

    public void setEvaluationOpinion(String evaluationOpinion) {
        this.evaluationOpinion = evaluationOpinion;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public Boolean getNeedReshoot() {
        return needReshoot;
    }

    public void setNeedReshoot(Boolean needReshoot) {
        this.needReshoot = needReshoot;
    }

    public String getReshootReason() {
        return reshootReason;
    }

    public void setReshootReason(String reshootReason) {
        this.reshootReason = reshootReason;
    }
}
