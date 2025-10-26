package swd.fpt.exegroupingmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfileEntity, Long>, JpaSpecificationExecutor<MentorProfileEntity> {
    Optional<MentorProfileEntity> findByUser_UserId(Long userId);
}
