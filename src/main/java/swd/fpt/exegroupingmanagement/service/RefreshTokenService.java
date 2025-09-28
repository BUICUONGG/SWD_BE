package swd.fpt.exegroupingmanagement.service;

import swd.fpt.exegroupingmanagement.dto.response.RefreshTokenResponse;
import swd.fpt.exegroupingmanagement.entity.RefreshTokenEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(UserEntity user);

    RefreshTokenEntity findByToken(String token);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    RefreshTokenResponse refreshToken(String requestRefreshToken);

}
