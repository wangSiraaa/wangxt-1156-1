package com.windfarm.defect.repository;

import com.windfarm.defect.entity.GenerationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationPlanRepository extends JpaRepository<GenerationPlan, Long> {

    List<GenerationPlan> findByIsDeletedFalse();

    Optional<GenerationPlan> findByIdAndIsDeletedFalse(Long id);

    List<GenerationPlan> findByTurbineIdAndIsDeletedFalse(Long turbineId);

    List<GenerationPlan> findByTurbineIdAndStatusAndIsDeletedFalse(Long turbineId, String status);

    List<GenerationPlan> findByPlanDateAndIsDeletedFalse(LocalDate planDate);

    List<GenerationPlan> findByTurbineIdAndIsDeletedFalseOrderByPlanDateDesc(Long turbineId);

    List<GenerationPlan> findByStatusAndIsDeletedFalse(String status);

    @Query("SELECT g FROM GenerationPlan g WHERE g.turbineId = :turbineId AND g.status = 'PUBLISHED' AND g.isDeleted = false")
    List<GenerationPlan> findActivePlansByTurbineId(@Param("turbineId") Long turbineId);

    boolean existsByPlanCode(String planCode);
}
