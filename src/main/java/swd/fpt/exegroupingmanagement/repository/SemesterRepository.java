package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;
import swd.fpt.exegroupingmanagement.enums.SemesterStatus;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterEntity, Long> {
    
    Optional<SemesterEntity> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<SemesterEntity> findByYear(Integer year);
    
    List<SemesterEntity> findByTerm(SemesterTerm term);
    
    List<SemesterEntity> findByStatus(SemesterStatus status);
    
    Optional<SemesterEntity> findByIsCurrentTrue();
    
    List<SemesterEntity> findByIsActiveTrue();
    
    List<SemesterEntity> findByIsDeletedFalse();
    
    @Query("SELECT s FROM SemesterEntity s WHERE s.startDate <= ?1 AND s.endDate >= ?1 AND s.isDeleted = false")
    Optional<SemesterEntity> findByDate(LocalDate date);
    
    @Query("SELECT s FROM SemesterEntity s WHERE s.year = ?1 AND s.term = ?2 AND s.isDeleted = false")
    Optional<SemesterEntity> findByYearAndTerm(Integer year, SemesterTerm term);
    
    @Query("SELECT s FROM SemesterEntity s WHERE s.status = ?1 AND s.isActive = true AND s.isDeleted = false ORDER BY s.startDate DESC")
    List<SemesterEntity> findActiveByStatus(SemesterStatus status);
}

