package swd.fpt.exegroupingmanagement.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import swd.fpt.exegroupingmanagement.entity.RefreshTokenEntity;

public interface RefreshTokenRepository  extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.expiryAt < :expiryAt")
    int deleteByExpiryAtBefore(@Param("expiryAt") LocalDateTime expiryAtBefore);

    Optional<RefreshTokenEntity> findByJwtId(String jwtId);
}
