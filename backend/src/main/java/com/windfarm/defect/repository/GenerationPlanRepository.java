package com.windfarm.defect.repository;

import com.windfarm.defect.entity.GenerationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenerationPlanRepository extends JpaRepository<GenerationPlan, Long> {

    List<GenerationPlan> findByTurbineIdAndIsDeletedFalseOrderByPlanDateDesc(Long turbineId);

    List<GenerationPlan> findByStatusAndIsDeletedFalse(String status);

    @Query("SELECT g FROM GenerationPlan g WHERE g.turbineId = :turbineId AND g.status = 'PUBLISHED' AND g.isDeleted = false")
    List<GenerationPlan> findActivePlansByTurbineId(@Param("turbineId") Long turbineId);

    boolean existsByPlanCode(String planCode);
}
