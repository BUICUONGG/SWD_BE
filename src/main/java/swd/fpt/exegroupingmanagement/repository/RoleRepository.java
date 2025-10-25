package swd.fpt.exegroupingmanagement.repository;

import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);
    Optional<RoleEntity> findByRoleNameAndIsDeletedFalse(String roleName);


}
