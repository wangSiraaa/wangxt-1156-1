package com.windfarm.defect.entity;

import com.windfarm.defect.enums.TurbineStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wind_turbine")
public class WindTurbine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turbine_code", nullable = false, unique = true, length = 50)
    private String turbineCode;

    @Column(name = "turbine_name", length = 100)
    private String turbineName;

    @Column(name = "wind_farm", length = 100)
    private String windFarm;

    @Column(name = "capacity_kw")
    private BigDecimal capacityKw;

    @Column(name = "blade_count")
    private Integer bladeCount;

    @Column(name = "tower_height")
    private BigDecimal towerHeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TurbineStatus status;

    @Column(name = "location_desc", length = 255)
    private String locationDesc;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "create_by", length = 50)
    private String createBy;

    @Column(name = "update_by", length = 50)
    private String updateBy;

    public WindTurbine() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTurbineCode() {
        return turbineCode;
    }

    public void setTurbineCode(String turbineCode) {
        this.turbineCode = turbineCode;
    }

    public String getTurbineName() {
        return turbineName;
    }

    public void setTurbineName(String turbineName) {
        this.turbineName = turbineName;
    }

    public String getWindFarm() {
        return windFarm;
    }

    public void setWindFarm(String windFarm) {
        this.windFarm = windFarm;
    }

    public BigDecimal getCapacityKw() {
        return capacityKw;
    }

    public void setCapacityKw(BigDecimal capacityKw) {
        this.capacityKw = capacityKw;
    }

    public Integer getBladeCount() {
        return bladeCount;
    }

    public void setBladeCount(Integer bladeCount) {
        this.bladeCount = bladeCount;
    }

    public BigDecimal getTowerHeight() {
        return towerHeight;
    }

    public void setTowerHeight(BigDecimal towerHeight) {
        this.towerHeight = towerHeight;
    }

    public TurbineStatus getStatus() {
        return status;
    }

    public void setStatus(TurbineStatus status) {
        this.status = status;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
