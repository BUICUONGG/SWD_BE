package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<IdeaEntity, Long> {
    List<IdeaEntity> findByEnrollment(EnrollmentEntity enrollment);
}
