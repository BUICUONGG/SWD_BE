package swd.fpt.exegroupingmanagement.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import  swd.fpt.exegroupingmanagement.entity.UserEntity;

import java.text.ParseException;

public interface JwtService {
    String extractEmail(String token);
    String extractId(String token);
    String generateRefreshToken(UserEntity user);
    String generateAccessToken(UserEntity user);
    String signToken(JWTClaimsSet claims);
    SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException;
}
