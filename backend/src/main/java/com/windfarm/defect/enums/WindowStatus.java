package com.windfarm.defect.enums;

public enum WindowStatus {
    PROPOSED("待评估"),
    CONFIRMED("已确认"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    WindowStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
