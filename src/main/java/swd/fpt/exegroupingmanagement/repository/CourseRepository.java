package swd.fpt.exegroupingmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
