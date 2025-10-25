package swd.fpt.exegroupingmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.MajorEntity;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    Optional<MajorEntity> findByCode(String code);
    boolean existsByCode(String code);
}
