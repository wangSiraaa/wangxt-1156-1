package com.windfarm.defect.enums;

public enum ReviewConclusion {
    CONFIRMED_DEFECT("确认缺陷"),
    FALSE_ALARM("误报"),
    NEED_OBSERVATION("待观察"),
    NEED_MAINTENANCE("需检修"),
    NEED_IMMEDIATE_MAINTENANCE("需立即检修");

    private final String description;

    ReviewConclusion(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
