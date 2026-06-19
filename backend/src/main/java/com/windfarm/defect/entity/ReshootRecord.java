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

    @Column(name = "blade_number")
    private Integer bladeNumber;

    @Column(name = "blade_position", length = 50)
    private String bladePosition;

    @Column(name = "required_angle")
    private BigDecimal requiredAngle;

    @Column(name = "required_azimuth")
    private BigDecimal requiredAzimuth;

    @Column(name = "actual_angle")
    private BigDecimal actualAngle;

    @Column(name = "actual_azimuth")
    private BigDecimal actualAzimuth;

    @Column(name = "angle_tolerance")
    private BigDecimal angleTolerance = new BigDecimal("5.0");

    @Column(name = "angle_deviation")
    private BigDecimal angleDeviation;

    @Column(name = "comparison_result", length = 20)
    private String comparisonResult;

    @Column(name = "comparison_opinion", length = 1000)
    private String comparisonOpinion;

    @Column(name = "is_angle_valid")
    private Boolean isAngleValid;

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

    public Integer getBladeNumber() {
        return bladeNumber;
    }

    public void setBladeNumber(Integer bladeNumber) {
        this.bladeNumber = bladeNumber;
    }

    public String getBladePosition() {
        return bladePosition;
    }

    public void setBladePosition(String bladePosition) {
        this.bladePosition = bladePosition;
    }

    public BigDecimal getRequiredAngle() {
        return requiredAngle;
    }

    public void setRequiredAngle(BigDecimal requiredAngle) {
        this.requiredAngle = requiredAngle;
    }

    public BigDecimal getRequiredAzimuth() {
        return requiredAzimuth;
    }

    public void setRequiredAzimuth(BigDecimal requiredAzimuth) {
        this.requiredAzimuth = requiredAzimuth;
    }

    public BigDecimal getActualAngle() {
        return actualAngle;
    }

    public void setActualAngle(BigDecimal actualAngle) {
        this.actualAngle = actualAngle;
    }

    public BigDecimal getActualAzimuth() {
        return actualAzimuth;
    }

    public void setActualAzimuth(BigDecimal actualAzimuth) {
        this.actualAzimuth = actualAzimuth;
    }

    public BigDecimal getAngleTolerance() {
        return angleTolerance;
    }

    public void setAngleTolerance(BigDecimal angleTolerance) {
        this.angleTolerance = angleTolerance;
    }

    public BigDecimal getAngleDeviation() {
        return angleDeviation;
    }

    public void setAngleDeviation(BigDecimal angleDeviation) {
        this.angleDeviation = angleDeviation;
    }

    public String getComparisonResult() {
        return comparisonResult;
    }

    public void setComparisonResult(String comparisonResult) {
        this.comparisonResult = comparisonResult;
    }

    public String getComparisonOpinion() {
        return comparisonOpinion;
    }

    public void setComparisonOpinion(String comparisonOpinion) {
        this.comparisonOpinion = comparisonOpinion;
    }

    public Boolean getIsAngleValid() {
        return isAngleValid;
    }

    public void setIsAngleValid(Boolean angleValid) {
        isAngleValid = angleValid;
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
