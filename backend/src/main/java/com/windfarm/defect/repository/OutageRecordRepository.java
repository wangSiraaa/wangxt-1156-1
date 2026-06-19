package com.windfarm.defect.repository;

import com.windfarm.defect.entity.OutageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutageRecordRepository extends JpaRepository<OutageRecord, Long> {

    List<OutageRecord> findByTurbineIdAndIsDeletedFalseOrderByCreateTimeDesc(Long turbineId);

    List<OutageRecord> findByDefectIdAndIsDeletedFalse(Long defectId);

    List<OutageRecord> findByIsActiveTrueAndIsDeletedFalse();

    @Query("SELECT o FROM OutageRecord o WHERE o.turbineId = :turbineId AND o.isActive = true AND o.isDeleted = false")
    Optional<OutageRecord> findActiveByTurbineId(@Param("turbineId") Long turbineId);

    @Query("SELECT COUNT(o) > 0 FROM OutageRecord o WHERE o.turbineId = :turbineId AND o.isActive = true AND o.isDeleted = false")
    boolean existsActiveByTurbineId(@Param("turbineId") Long turbineId);

    boolean existsByOutageCode(String outageCode);
}
