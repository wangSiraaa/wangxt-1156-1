package com.windfarm.defect.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
}
