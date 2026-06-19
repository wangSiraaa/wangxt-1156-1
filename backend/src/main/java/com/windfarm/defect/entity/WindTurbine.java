package com.windfarm.defect.entity;

import com.windfarm.defect.enums.TurbineStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
}
