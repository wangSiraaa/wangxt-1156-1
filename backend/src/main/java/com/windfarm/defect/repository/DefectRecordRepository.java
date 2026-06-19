package com.windfarm.defect.repository;

import com.windfarm.defect.entity.DefectRecord;
import com.windfarm.defect.enums.DefectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefectRecordRepository extends JpaRepository<DefectRecord, Long> {

    List<DefectRecord> findByTurbineIdAndIsDeletedFalse(Long turbineId);

    List<DefectRecord> findByStatusAndIsDeletedFalse(DefectStatus status);

    List<DefectRecord> findByTurbineIdAndStatusAndIsDeletedFalse(Long turbineId, DefectStatus status);

    List<DefectRecord> findByIsDeletedFalse();

    @Query("SELECT d FROM DefectRecord d WHERE d.turbineId = :turbineId AND d.status IN :statuses AND d.isDeleted = false ORDER BY d.createTime DESC")
    List<DefectRecord> findByTurbineIdAndStatusIn(@Param("turbineId") Long turbineId, @Param("statuses") List<DefectStatus> statuses);

    @Query("SELECT COUNT(d) > 0 FROM DefectRecord d WHERE d.turbineId = :turbineId AND d.status = :status AND d.isDeleted = false")
    boolean existsByTurbineIdAndStatus(Long turbineId, DefectStatus status);

    boolean existsByDefectCode(String defectCode);
}
