package com.windfarm.defect.entity;

import com.windfarm.defect.enums.DefectStatus;
import com.windfarm.defect.enums.DefectType;
import com.windfarm.defect.enums.ReviewConclusion;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "defect_record")
public class DefectRecord {

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
}
