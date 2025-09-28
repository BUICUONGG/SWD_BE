package swd.fpt.exegroupingmanagement.service.impl;

import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.dto.response.RefreshTokenResponse;
import swd.fpt.exegroupingmanagement.entity.RefreshTokenEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.UnauthorizedException;
import swd.fpt.exegroupingmanagement.repository.RefreshTokenRepository;
import swd.fpt.exegroupingmanagement.service.JwtService;
import swd.fpt.exegroupingmanagement.service.RefreshTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;
    JwtService jwtService;

    @Override
    public RefreshTokenEntity createRefreshToken(UserEntity user) {
        String refreshTokenJwt = jwtService.generateRefreshToken(user);
        String jti;
        Date expiry;
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshTokenJwt);
            jti = signedJWT.getJWTClaimsSet().getJWTID();
            expiry = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new IllegalStateException("Cannot parse generated refresh token", e);
        }

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .jwtId(jti)
                .token(refreshTokenJwt)
                .expiryAt(expiry.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshTokenEntity  findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new UnauthorizedException("Refresh token not found or invalid"));
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
        return token;
    }

    @Override
    public RefreshTokenResponse refreshToken(String requestRefreshToken) {
        try {
            SignedJWT signedJWT = jwtService.verifyToken(requestRefreshToken, true);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            return refreshTokenRepository.findByJwtId(jti)
                    .map(this::verifyExpiration)
                    .map(RefreshTokenEntity::getUser)
                    .map(user -> {
                        String newAccessToken = jwtService.generateAccessToken(user);
                        return new RefreshTokenResponse(newAccessToken);
                    })
                    .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
    }



    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = refreshTokenRepository.deleteByExpiryAtBefore(now);
        if (deletedCount > 0) {
            log.info("🧹 Deleted {} expired refresh tokens before {}", deletedCount, now);
        }
    }
}
