package com.windfarm.defect.enums;

public enum TurbineStatus {
    RUNNING("运行中"),
    STOPPED("已停机"),
    MAINTENANCE("检修中"),
    STANDBY("备用"),
    FAULT("故障");

    private final String description;

    TurbineStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
