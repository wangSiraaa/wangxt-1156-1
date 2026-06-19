package com.windfarm.defect.entity;

import com.windfarm.defect.enums.WindowStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_window")
public class MaintenanceWindow {

    public MaintenanceWindow() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "window_code", unique = true, length = 50)
    private String windowCode;

    @Column(name = "defect_id")
    private Long defectId;

    @Column(name = "turbine_id", nullable = false)
    private Long turbineId;

    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "expected_wind_speed")
    private BigDecimal expectedWindSpeed;

    @Column(name = "actual_wind_speed")
    private BigDecimal actualWindSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private WindowStatus status;

    @Column(name = "window_type", length = 20)
    private String windowType;

    @Column(name = "maintenance_content", length = 1000)
    private String maintenanceContent;

    @Column(name = "maintenance_team", length = 100)
    private String maintenanceTeam;

    @Column(name = "evaluator", length = 50)
    private String evaluator;

    @Column(name = "evaluation_time")
    private LocalDateTime evaluationTime;

    @Column(name = "evaluation_opinion", length = 1000)
    private String evaluationOpinion;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWindowCode() {
        return windowCode;
    }

    public void setWindowCode(String windowCode) {
        this.windowCode = windowCode;
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

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalDateTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public BigDecimal getExpectedWindSpeed() {
        return expectedWindSpeed;
    }

    public void setExpectedWindSpeed(BigDecimal expectedWindSpeed) {
        this.expectedWindSpeed = expectedWindSpeed;
    }

    public BigDecimal getActualWindSpeed() {
        return actualWindSpeed;
    }

    public void setActualWindSpeed(BigDecimal actualWindSpeed) {
        this.actualWindSpeed = actualWindSpeed;
    }

    public WindowStatus getStatus() {
        return status;
    }

    public void setStatus(WindowStatus status) {
        this.status = status;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getMaintenanceTeam() {
        return maintenanceTeam;
    }

    public void setMaintenanceTeam(String maintenanceTeam) {
        this.maintenanceTeam = maintenanceTeam;
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

    public String getEvaluationOpinion() {
        return evaluationOpinion;
    }

    public void setEvaluationOpinion(String evaluationOpinion) {
        this.evaluationOpinion = evaluationOpinion;
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
