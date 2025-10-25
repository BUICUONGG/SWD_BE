package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.enums.EnrollmentStatus;
import swd.fpt.exegroupingmanagement.enums.EnrollmentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
    
    Optional<EnrollmentEntity> findByUserIdAndCourseId(Long userId, Long courseId);
    
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    
    List<EnrollmentEntity> findByUserId(Long userId);
    
    List<EnrollmentEntity> findByCourseId(Long courseId);
    
    List<EnrollmentEntity> findByStatus(EnrollmentStatus status);
    
    List<EnrollmentEntity> findByEnrollmentType(EnrollmentType type);
    
    List<EnrollmentEntity> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status);
    
    List<EnrollmentEntity> findByUserIdAndStatus(Long userId, EnrollmentStatus status);
    
    List<EnrollmentEntity> findByTeamId(Long teamId);
    
    List<EnrollmentEntity> findByIsTeamLeaderTrue();
    
    List<EnrollmentEntity> findByIsDeletedFalse();
    
    @Query("SELECT e FROM EnrollmentEntity e WHERE e.courseId = ?1 AND e.status = 'ENROLLED' AND e.isDeleted = false")
    List<EnrollmentEntity> findEnrolledStudentsByCourse(Long courseId);
    
    @Query("SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.courseId = ?1 AND e.status = 'ENROLLED' AND e.isDeleted = false")
    long countEnrolledStudentsByCourse(Long courseId);
    
    @Query("SELECT e FROM EnrollmentEntity e WHERE e.userId = ?1 AND e.status = 'ENROLLED' AND e.isDeleted = false")
    List<EnrollmentEntity> findActiveEnrollmentsByUser(Long userId);
    
    @Query("SELECT e FROM EnrollmentEntity e WHERE e.courseId = ?1 AND e.teamId IS NULL AND e.status = 'ENROLLED' AND e.isDeleted = false")
    List<EnrollmentEntity> findStudentsWithoutTeam(Long courseId);
}

