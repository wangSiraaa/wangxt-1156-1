package com.windfarm.defect.enums;

public enum DefectType {
    CRACK_SUSPECTED("疑似裂纹"),
    SURFACE_CRACK("表面裂纹"),
    INTERNAL_CRACK("内部裂纹"),
    CORROSION("腐蚀"),
    ABRASION("磨损"),
    LIGHTNING_STRIKE("雷击损伤"),
    BLADE_DIRTY("叶片脏污"),
    PAINT_PEELING("油漆脱落"),
    OTHER("其他");

    private final String description;

    DefectType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
