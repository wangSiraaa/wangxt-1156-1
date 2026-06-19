package com.windfarm.defect.entity;

import com.windfarm.defect.enums.TurbineStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "outage_record")
public class OutageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "outage_code", unique = true, length = 50)
    private String outageCode;

    @Column(name = "turbine_id", nullable = false)
    private Long turbineId;

    @Column(name = "defect_id")
    private Long defectId;

    @Column(name = "window_id")
    private Long windowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "turbine_status_before", length = 20)
    private TurbineStatus turbineStatusBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "turbine_status_after", length = 20)
    private TurbineStatus turbineStatusAfter;

    @Column(name = "outage_reason", length = 500)
    private String outageReason;

    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "operator", length = 50)
    private String operator;

    @Column(name = "approver", length = 50)
    private String approver;

    @Column(name = "approval_time")
    private LocalDateTime approvalTime;

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

    public OutageRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutageCode() {
        return outageCode;
    }

    public void setOutageCode(String outageCode) {
        this.outageCode = outageCode;
    }

    public Long getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(Long turbineId) {
        this.turbineId = turbineId;
    }

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public Long getWindowId() {
        return windowId;
    }

    public void setWindowId(Long windowId) {
        this.windowId = windowId;
    }

    public TurbineStatus getTurbineStatusBefore() {
        return turbineStatusBefore;
    }

    public void setTurbineStatusBefore(TurbineStatus turbineStatusBefore) {
        this.turbineStatusBefore = turbineStatusBefore;
    }

    public TurbineStatus getTurbineStatusAfter() {
        return turbineStatusAfter;
    }

    public void setTurbineStatusAfter(TurbineStatus turbineStatusAfter) {
        this.turbineStatusAfter = turbineStatusAfter;
    }

    public String getOutageReason() {
        return outageReason;
    }

    public void setOutageReason(String outageReason) {
        this.outageReason = outageReason;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public LocalDateTime getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(LocalDateTime approvalTime) {
        this.approvalTime = approvalTime;
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
