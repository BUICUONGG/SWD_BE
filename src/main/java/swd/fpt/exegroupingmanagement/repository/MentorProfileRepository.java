package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfileEntity, Long> {
    
    Optional<MentorProfileEntity> findByUserId(Long userId);
    
    Optional<MentorProfileEntity> findByEmployeeCode(String employeeCode);
    
    boolean existsByUserId(Long userId);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    List<MentorProfileEntity> findByDepartment(String department);
    
    List<MentorProfileEntity> findByIsAvailable(Boolean isAvailable);
    
    List<MentorProfileEntity> findByIsDeletedFalse();
    
    @Query("SELECT mp FROM MentorProfileEntity mp WHERE mp.isDeleted = false AND mp.isAvailable = true " +
           "AND (mp.maxCoursesPerSemester IS NULL OR mp.currentCourseCount < mp.maxCoursesPerSemester)")
    List<MentorProfileEntity> findAvailableMentors();
    
    @Query("SELECT mp FROM MentorProfileEntity mp " +
           "JOIN mp.user u WHERE u.status = 'ACTIVE' AND mp.isDeleted = false AND mp.isAvailable = true " +
           "ORDER BY mp.averageRating DESC")
    List<MentorProfileEntity> findAllOrderByRating();
}

