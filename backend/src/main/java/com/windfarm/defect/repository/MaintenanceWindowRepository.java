package com.windfarm.defect.repository;

import com.windfarm.defect.entity.MaintenanceWindow;
import com.windfarm.defect.enums.WindowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindow, Long> {

    List<MaintenanceWindow> findByTurbineIdAndIsDeletedFalse(Long turbineId);

    List<MaintenanceWindow> findByDefectIdAndIsDeletedFalse(Long defectId);

    List<MaintenanceWindow> findByStatusAndIsDeletedFalse(WindowStatus status);

    List<MaintenanceWindow> findByIsDeletedFalse();

    @Query("SELECT w FROM MaintenanceWindow w WHERE w.turbineId = :turbineId AND w.status IN :statuses AND w.isDeleted = false")
    List<MaintenanceWindow> findByTurbineIdAndStatusIn(@Param("turbineId") Long turbineId, @Param("statuses") List<WindowStatus> statuses);

    @Query("SELECT COUNT(w) > 0 FROM MaintenanceWindow w WHERE w.turbineId = :turbineId AND w.status = :status AND w.isDeleted = false")
    boolean existsByTurbineIdAndStatus(Long turbineId, WindowStatus status);

    @Query("SELECT w FROM MaintenanceWindow w WHERE w.turbineId = :turbineId AND w.plannedStartTime >= :startTime AND w.plannedEndTime <= :endTime AND w.isDeleted = false")
    List<MaintenanceWindow> findByTurbineIdAndTimeRange(@Param("turbineId") Long turbineId,
                                                        @Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    boolean existsByWindowCode(String windowCode);
}
