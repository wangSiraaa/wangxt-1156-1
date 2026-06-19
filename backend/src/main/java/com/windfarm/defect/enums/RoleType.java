package com.windfarm.defect.enums;

public enum RoleType {
    DRONE_INSPECTOR("无人机巡检员"),
    MAINTENANCE_TEAM("检修班"),
    STATION_MANAGER("场站负责人"),
    ADMIN("系统管理员");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
