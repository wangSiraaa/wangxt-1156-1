package com.windfarm.defect.entity;

import com.windfarm.defect.enums.WindowStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "maintenance_window")
public class MaintenanceWindow {

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
}
