package swd.fpt.exegroupingmanagement.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {
    Optional<CourseEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<CourseEntity> findByStatus(CourseStatus status);
    List<CourseEntity> findBySemester_SemesterId(Long semesterId);
    List<CourseEntity> findByMentor_UserId(Long mentorId);
    
    // Query courses that need status update
    @Query("SELECT c FROM CourseEntity c WHERE c.status = :status AND c.semester.startDate <= :currentDate AND c.deleted = false")
    List<CourseEntity> findUpcomingCoursesToOpen(@Param("status") CourseStatus status, @Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT c FROM CourseEntity c WHERE c.status = :status AND c.teamFormationDeadline <= :currentDateTime AND c.deleted = false")
    List<CourseEntity> findOpenCoursesToStart(@Param("status") CourseStatus status, @Param("currentDateTime") LocalDateTime currentDateTime);
    
    @Query("SELECT c FROM CourseEntity c WHERE c.status = :status AND c.semester.endDate <= :currentDate AND c.deleted = false")
    List<CourseEntity> findInProgressCoursesToComplete(@Param("status") CourseStatus status, @Param("currentDate") LocalDate currentDate);
}
