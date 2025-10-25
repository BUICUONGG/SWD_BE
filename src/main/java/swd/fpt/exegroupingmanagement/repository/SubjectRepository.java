package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;
import swd.fpt.exegroupingmanagement.enums.SubjectType;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    
    Optional<SubjectEntity> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<SubjectEntity> findByMajorId(Long majorId);
    
    List<SubjectEntity> findBySubjectType(SubjectType subjectType);
    
    List<SubjectEntity> findByLevel(Integer level);
    
    List<SubjectEntity> findByRequiresProjectTrue();
    
    List<SubjectEntity> findByRequiresTeamTrue();
    
    List<SubjectEntity> findByIsActiveTrue();
    
    List<SubjectEntity> findByIsDeletedFalse();
    
    @Query("SELECT s FROM SubjectEntity s WHERE s.majorId = ?1 AND s.isActive = true AND s.isDeleted = false")
    List<SubjectEntity> findActiveSubjectsByMajor(Long majorId);
    
    @Query("SELECT s FROM SubjectEntity s WHERE s.subjectType = ?1 AND s.isActive = true AND s.isDeleted = false")
    List<SubjectEntity> findActiveSubjectsByType(SubjectType type);
}

