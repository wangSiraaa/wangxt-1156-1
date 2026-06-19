package com.windfarm.defect.entity;

import com.windfarm.defect.enums.DefectStatus;
import com.windfarm.defect.enums.DefectType;
import com.windfarm.defect.enums.ReviewConclusion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "defect_record")
public class DefectRecord {

    public DefectRecord() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "defect_code", unique = true, length = 50)
    private String defectCode;

    @Column(name = "turbine_id", nullable = false)
    private Long turbineId;

    @Column(name = "blade_number", nullable = false)
    private Integer bladeNumber;

    @Column(name = "blade_position", length = 50)
    private String bladePosition;

    @Column(name = "blade_mapping_info", length = 500)
    private String bladeMappingInfo;

    @Column(name = "shooting_angle")
    private BigDecimal shootingAngle;

    @Column(name = "shooting_azimuth")
    private BigDecimal shootingAzimuth;

    @Column(name = "defect_description", length = 1000)
    private String defectDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "defect_type", length = 30)
    private DefectType defectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private DefectStatus status;

    @Column(name = "severity_level", length = 20)
    private String severityLevel;

    @Column(name = "defect_size")
    private BigDecimal defectSize;

    @Column(name = "size_unit", length = 10)
    private String sizeUnit;

    @Column(name = "photo_urls", length = 1000)
    private String photoUrls;

    @Column(name = "inspection_time")
    private LocalDateTime inspectionTime;

    @Column(name = "inspector", length = 50)
    private String inspector;

    @Column(name = "wind_speed_inspection")
    private BigDecimal windSpeedInspection;

    @Column(name = "reshoot_count", nullable = false)
    private Integer reshootCount = 0;

    @Column(name = "need_reshoot", nullable = false)
    private Boolean needReshoot = false;

    @Column(name = "evaluation_opinion", length = 1000)
    private String evaluationOpinion;

    @Column(name = "evaluator", length = 50)
    private String evaluator;

    @Column(name = "evaluation_time")
    private LocalDateTime evaluationTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_conclusion", length = 30)
    private ReviewConclusion reviewConclusion;

    @Column(name = "review_opinion", length = 1000)
    private String reviewOpinion;

    @Column(name = "reviewer", length = 50)
    private String reviewer;

    @Column(name = "review_time")
    private LocalDateTime reviewTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
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

    public String getBladeMappingInfo() {
        return bladeMappingInfo;
    }

    public void setBladeMappingInfo(String bladeMappingInfo) {
        this.bladeMappingInfo = bladeMappingInfo;
    }

    public BigDecimal getShootingAngle() {
        return shootingAngle;
    }

    public void setShootingAngle(BigDecimal shootingAngle) {
        this.shootingAngle = shootingAngle;
    }

    public BigDecimal getShootingAzimuth() {
        return shootingAzimuth;
    }

    public void setShootingAzimuth(BigDecimal shootingAzimuth) {
        this.shootingAzimuth = shootingAzimuth;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public DefectStatus getStatus() {
        return status;
    }

    public void setStatus(DefectStatus status) {
        this.status = status;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public BigDecimal getDefectSize() {
        return defectSize;
    }

    public void setDefectSize(BigDecimal defectSize) {
        this.defectSize = defectSize;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public LocalDateTime getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(LocalDateTime inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public BigDecimal getWindSpeedInspection() {
        return windSpeedInspection;
    }

    public void setWindSpeedInspection(BigDecimal windSpeedInspection) {
        this.windSpeedInspection = windSpeedInspection;
    }

    public Integer getReshootCount() {
        return reshootCount;
    }

    public void setReshootCount(Integer reshootCount) {
        this.reshootCount = reshootCount;
    }

    public Boolean getNeedReshoot() {
        return needReshoot;
    }

    public void setNeedReshoot(Boolean needReshoot) {
        this.needReshoot = needReshoot;
    }

    public String getEvaluationOpinion() {
        return evaluationOpinion;
    }

    public void setEvaluationOpinion(String evaluationOpinion) {
        this.evaluationOpinion = evaluationOpinion;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public LocalDateTime getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(LocalDateTime evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public ReviewConclusion getReviewConclusion() {
        return reviewConclusion;
    }

    public void setReviewConclusion(ReviewConclusion reviewConclusion) {
        this.reviewConclusion = reviewConclusion;
    }

    public String getReviewOpinion() {
        return reviewOpinion;
    }

    public void setReviewOpinion(String reviewOpinion) {
        this.reviewOpinion = reviewOpinion;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
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
