package com.windfarm.defect.entity;

import com.windfarm.defect.enums.TurbineStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
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
}
