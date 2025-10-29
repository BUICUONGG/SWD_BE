package swd.fpt.exegroupingmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.SemesterEntity;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterEntity, Long> {
    Optional<SemesterEntity> findByCode(String code);
    boolean existsByCode(String code);
}
