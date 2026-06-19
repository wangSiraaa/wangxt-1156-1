package com.windfarm.defect.enums;

public enum DefectStatus {
    PENDING_EVALUATION("待评估"),
    PENDING_RESHOOT("待复拍"),
    PENDING_MAINTENANCE("待检修"),
    CONFIRMED("已确认"),
    CLOSED("已关闭"),
    FALSE_ALARM("误报");

    private final String description;

    DefectStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
