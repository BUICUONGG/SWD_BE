package swd.fpt.exegroupingmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
    List<EnrollmentEntity> findByUser_UserId(Long userId);
    List<EnrollmentEntity> findByCourse_CourseId(Long courseId);
    Optional<EnrollmentEntity> findByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
    boolean existsByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
