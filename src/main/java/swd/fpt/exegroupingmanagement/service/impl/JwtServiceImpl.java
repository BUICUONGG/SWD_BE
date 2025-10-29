package swd.fpt.exegroupingmanagement.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;
import swd.fpt.exegroupingmanagement.exception.exceptions.UnauthorizedException;
import swd.fpt.exegroupingmanagement.service.JwtService;
import swd.fpt.exegroupingmanagement.service.RedisService;

@Service
public class JwtServiceImpl implements JwtService {
    private final RedisService redisService;
    @NonFinal
    @Value("${jwt.secret-key}")
    String secretKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long refreshableDuration;

    static final String ISSUER = "EXEGrouping.com";

    public JwtServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public String extractEmail(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getSubject();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid token format", e);
        }
    }
    @Override
    public String extractId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getJWTID(); // jti
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid token format", e);
        }
    }

    @Override
    public String generateRefreshToken(UserEntity user) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(refreshableDuration, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .claim("scope",buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        return signToken(claims);
    }

    @Override
    public String generateAccessToken(UserEntity user) {
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(validDuration, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString());
        return signToken(claimsBuilder.build());
    }

    @Override

    public SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new UnauthorizedException(ErrorCode.TOKEN_INCORRECT.getMessage());
        }
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        if (!isRefresh) {
            if (redisService.exists(jti)) {
                throw new UnauthorizedException(ErrorCode.UNAUTHENTICATED.getMessage());
            }
        }
        return signedJWT;
    }

    @Override
    public String signToken(JWTClaimsSet claims) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Cannot create JWT", e);
        }
    }
    private String buildScope(UserEntity user) {
        if (user.getRole() != null) {
            return user.getRole().getRoleName();
        }
        return "";
    }
}
