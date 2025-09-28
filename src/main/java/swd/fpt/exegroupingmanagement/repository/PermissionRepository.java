package swd.fpt.exegroupingmanagement.repository;

import swd.fpt.exegroupingmanagement.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    boolean existsByPermissionName(String permissionName);
    List<PermissionEntity> findAllByOrderByPermissionIdAsc();
    Optional<PermissionEntity> findByPermissionName(String permissionName);

}
