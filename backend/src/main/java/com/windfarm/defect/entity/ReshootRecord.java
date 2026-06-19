package com.windfarm.defect.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reshoot_record")
public class ReshootRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reshoot_code", unique = true, length = 50)
    private String reshootCode;

    @Column(name = "defect_id", nullable = false)
    private Long defectId;

    @Column(name = "turbine_id", nullable = false)
    private Long turbineId;

    @Column(name = "reshoot_reason", length = 500)
    private String reshootReason;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "actual_time")
    private LocalDateTime actualTime;

    @Column(name = "wind_speed_scheduled")
    private BigDecimal windSpeedScheduled;

    @Column(name = "wind_speed_actual")
    private BigDecimal windSpeedActual;

    @Column(name = "photo_urls", length = 1000)
    private String photoUrls;

    @Column(name = "reshoot_result", length = 1000)
    private String reshootResult;

    @Column(name = "reshoot_operator", length = 50)
    private String reshootOperator;

    @Column(name = "reshoot_order")
    private Integer reshootOrder;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

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

    public ReshootRecord() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReshootCode() {
        return reshootCode;
    }

    public void setReshootCode(String reshootCode) {
        this.reshootCode = reshootCode;
    }

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public Long getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(Long turbineId) {
        this.turbineId = turbineId;
    }

    public String getReshootReason() {
        return reshootReason;
    }

    public void setReshootReason(String reshootReason) {
        this.reshootReason = reshootReason;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getActualTime() {
        return actualTime;
    }

    public void setActualTime(LocalDateTime actualTime) {
        this.actualTime = actualTime;
    }

    public BigDecimal getWindSpeedScheduled() {
        return windSpeedScheduled;
    }

    public void setWindSpeedScheduled(BigDecimal windSpeedScheduled) {
        this.windSpeedScheduled = windSpeedScheduled;
    }

    public BigDecimal getWindSpeedActual() {
        return windSpeedActual;
    }

    public void setWindSpeedActual(BigDecimal windSpeedActual) {
        this.windSpeedActual = windSpeedActual;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getReshootResult() {
        return reshootResult;
    }

    public void setReshootResult(String reshootResult) {
        this.reshootResult = reshootResult;
    }

    public String getReshootOperator() {
        return reshootOperator;
    }

    public void setReshootOperator(String reshootOperator) {
        this.reshootOperator = reshootOperator;
    }

    public Integer getReshootOrder() {
        return reshootOrder;
    }

    public void setReshootOrder(Integer reshootOrder) {
        this.reshootOrder = reshootOrder;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
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
