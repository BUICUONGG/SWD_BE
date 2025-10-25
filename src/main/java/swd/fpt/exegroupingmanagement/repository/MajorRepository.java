package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.MajorEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    
    Optional<MajorEntity> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<MajorEntity> findByIsActiveTrue();
    
    List<MajorEntity> findByIsAcceptingStudentsTrue();
    
    List<MajorEntity> findByDepartment(String department);
    
    List<MajorEntity> findByFaculty(String faculty);
    
    List<MajorEntity> findByIsDeletedFalse();
    
    List<MajorEntity> findByIsActiveTrueAndIsDeletedFalse();
}

