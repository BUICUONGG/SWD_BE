package swd.fpt.exegroupingmanagement.repository;

import swd.fpt.exegroupingmanagement.entity.MajorEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRole(RoleEntity role);
    List<UserEntity> findByRole(RoleEntity role);
    List<UserEntity> findByMajor(MajorEntity major);
}
