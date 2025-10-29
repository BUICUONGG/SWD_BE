package swd.fpt.exegroupingmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import swd.fpt.exegroupingmanagement.entity.DeviceTokenEntity;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceTokenEntity, Long> {

    Optional<DeviceTokenEntity> findByToken(String token);
    
    /**
     * Tìm tất cả tokens active của một user
     * Dùng để gửi notification đến tất cả thiết bị của user
     */
    @Query("SELECT dt FROM DeviceTokenEntity dt WHERE dt.user.userId = :userId AND dt.isActive = true")
    List<DeviceTokenEntity> findActiveTokensByUserId(@Param("userId") Long userId);
    
    /**
     * Tìm token của user và device type cụ thể
     */
    @Query("SELECT dt FROM DeviceTokenEntity dt WHERE dt.user.userId = :userId AND dt.deviceType = :deviceType AND dt.isActive = true")
    List<DeviceTokenEntity> findActiveTokensByUserIdAndDeviceType(
        @Param("userId") Long userId, 
        @Param("deviceType") swd.fpt.exegroupingmanagement.enums.DeviceType deviceType
    );
    
    /**
     * Deactivate token (khi user logout)
     */
    @Modifying
    @Query("UPDATE DeviceTokenEntity dt SET dt.isActive = false WHERE dt.token = :token")
    int deactivateToken(@Param("token") String token);
    
    /**
     * Deactivate tất cả tokens của user (khi user logout khỏi tất cả thiết bị)
     */
    @Modifying
    @Query("UPDATE DeviceTokenEntity dt SET dt.isActive = false WHERE dt.user.userId = :userId")
    int deactivateAllUserTokens(@Param("userId") Long userId);
    
    /**
     * Xóa các token cũ không được sử dụng trong X ngày
     * Dùng để cleanup database định kỳ
     */
    @Modifying
    @Query("DELETE FROM DeviceTokenEntity dt WHERE dt.lastUsedAt < :expiryDate")
    int deleteExpiredTokens(@Param("expiryDate") LocalDateTime expiryDate);
    
    /**
     * Update last used time
     */
    @Modifying
    @Query("UPDATE DeviceTokenEntity dt SET dt.lastUsedAt = :lastUsedAt WHERE dt.token = :token")
    int updateLastUsedAt(@Param("token") String token, @Param("lastUsedAt") LocalDateTime lastUsedAt);
}

