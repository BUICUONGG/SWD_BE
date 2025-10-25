package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    
    Optional<CourseEntity> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<CourseEntity> findByMentorId(Long mentorId);
    
    List<CourseEntity> findByMentorIdAndStatus(Long mentorId, CourseStatus status);
    
    List<CourseEntity> findBySubjectId(Long subjectId);
    
    List<CourseEntity> findBySemesterId(Long semesterId);
    
    List<CourseEntity> findByStatus(CourseStatus status);
    
    List<CourseEntity> findByIsActiveTrue();
    
    List<CourseEntity> findByIsDeletedFalse();
    
    @Query("SELECT c FROM CourseEntity c WHERE c.mentorId = ?1 AND c.status = 'IN_PROGRESS' AND c.isDeleted = false")
    List<CourseEntity> findActiveCoursesByMentor(Long mentorId);
    
    @Query("SELECT COUNT(c) FROM CourseEntity c WHERE c.mentorId = ?1 AND c.semesterId = ?2 AND c.isDeleted = false")
    long countByMentorIdAndSemesterId(Long mentorId, Long semesterId);
    
    @Query("SELECT c FROM CourseEntity c WHERE c.semesterId = ?1 AND c.isActive = true AND c.isDeleted = false")
    List<CourseEntity> findActiveCoursesBySemester(Long semesterId);
    
    @Query("SELECT c FROM CourseEntity c WHERE c.subjectId = ?1 AND c.semesterId = ?2 AND c.isDeleted = false")
    List<CourseEntity> findBySubjectAndSemester(Long subjectId, Long semesterId);
}

