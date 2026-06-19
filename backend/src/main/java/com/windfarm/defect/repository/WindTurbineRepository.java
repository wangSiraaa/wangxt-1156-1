package com.windfarm.defect.repository;

import com.windfarm.defect.entity.WindTurbine;
import com.windfarm.defect.enums.TurbineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WindTurbineRepository extends JpaRepository<WindTurbine, Long> {

    Optional<WindTurbine> findByTurbineCodeAndIsDeletedFalse(String turbineCode);

    List<WindTurbine> findByWindFarmAndIsDeletedFalse(String windFarm);

    List<WindTurbine> findByStatusAndIsDeletedFalse(TurbineStatus status);

    List<WindTurbine> findByIsDeletedFalse();

    @Query("SELECT COUNT(w) > 0 FROM WindTurbine w WHERE w.id = :id AND w.status = :status AND w.isDeleted = false")
    boolean existsByIdAndStatus(Long id, TurbineStatus status);

    boolean existsByTurbineCodeAndIsDeletedFalse(String turbineCode);
}
