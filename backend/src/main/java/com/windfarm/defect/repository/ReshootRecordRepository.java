package com.windfarm.defect.repository;

import com.windfarm.defect.entity.ReshootRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReshootRecordRepository extends JpaRepository<ReshootRecord, Long> {

    List<ReshootRecord> findByDefectIdAndIsDeletedFalseOrderByReshootOrderAsc(Long defectId);

    List<ReshootRecord> findByTurbineIdAndIsDeletedFalse(Long turbineId);

    List<ReshootRecord> findByIsCompletedFalseAndIsDeletedFalse();

    @Query("SELECT COUNT(r) FROM ReshootRecord r WHERE r.defectId = :defectId AND r.isDeleted = false")
    int countByDefectId(@Param("defectId") Long defectId);

    @Query("SELECT MAX(r.reshootOrder) FROM ReshootRecord r WHERE r.defectId = :defectId AND r.isDeleted = false")
    Integer findMaxReshootOrderByDefectId(@Param("defectId") Long defectId);

    boolean existsByReshootCode(String reshootCode);
}
