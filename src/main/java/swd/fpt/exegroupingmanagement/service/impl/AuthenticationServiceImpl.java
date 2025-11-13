package swd.fpt.exegroupingmanagement.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.request.ExchangeTokenRequest;
import swd.fpt.exegroupingmanagement.dto.request.IntrospectRequest;
import swd.fpt.exegroupingmanagement.dto.request.LogoutRequest;
import swd.fpt.exegroupingmanagement.dto.request.RefreshTokenRequest;
import swd.fpt.exegroupingmanagement.dto.request.RegisterRequest;
import swd.fpt.exegroupingmanagement.dto.request.UserLoginRequest;
import swd.fpt.exegroupingmanagement.dto.response.ExchangeTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.IntrospectResponse;
import swd.fpt.exegroupingmanagement.dto.response.RefreshTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.entity.RefreshTokenEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.Provider;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.exception.exceptions.UnauthorizedException;
import swd.fpt.exegroupingmanagement.mapper.UserMapper;
import swd.fpt.exegroupingmanagement.repository.RefreshTokenRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.repository.httpclient.OutboundAuthClient;
import swd.fpt.exegroupingmanagement.repository.httpclient.OutboundUserClient;
import swd.fpt.exegroupingmanagement.service.AuthenticationService;
import swd.fpt.exegroupingmanagement.service.JwtService;
import swd.fpt.exegroupingmanagement.service.MajorService;
import swd.fpt.exegroupingmanagement.service.RedisService;
import swd.fpt.exegroupingmanagement.service.RefreshTokenService;
import swd.fpt.exegroupingmanagement.service.RoleService;
import swd.fpt.exegroupingmanagement.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleService roleService;
    MajorService majorService;
    OutboundAuthClient outboundAuthClient;
    OutboundUserClient outboundUserClient;
    JwtService jwtService;
    private final RedisService redisService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    String clientId;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    String redirectUri;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    String clientSecret;

    static final String GRANT_TYPE = "authorization_code";

    static final String RESPONSE_FORMAT = "json";

    @Override
    public UserResponse register(RegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new ResourceConflictException(ErrorCode.USER_ALREADY_EXISTS.getMessage());
        }
        RoleEntity roleEntity = roleService.findByRoleName(PredefinedRole.ROLE_STUDENT);
        
        // Parse major code from email (e.g., vinhlqse182115@fpt.edu.vn -> SE)
        String majorCode = majorService.parseMajorCodeFromEmail(userRegisterRequest.getEmail());
        
        var userBuilder = UserEntity.builder()
                .email(userRegisterRequest.getEmail())
                .fullName(userRegisterRequest.getFullName())
                .dob(userRegisterRequest.getDob())
                .gender(userRegisterRequest.getGender())
                .passwordHash(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(roleEntity);
        
        // Set major if found
        if (majorCode != null) {
            try {
                userBuilder.major(majorService.getMajorEntityByCode(majorCode));
            } catch (ResourceNotFoundException e) {
                log.warn("Major code {} parsed from email {} not found in database", majorCode, userRegisterRequest.getEmail());
            }
        }
        
        UserEntity userEntity = userBuilder.build();
        userRepository.save(userEntity);
        return userMapper.toEntityDTO(userEntity);
    }
    @Override
    public UserResponse.UserLoginResponse login(UserLoginRequest userLoginRequest) {
        UserEntity user = userService.getActiveUser(userLoginRequest.getEmail());
        boolean authenticated = passwordEncoder.matches(userLoginRequest.getPassword(), user.getPasswordHash());
        if (!authenticated) {
            throw new UnauthorizedException(ErrorCode.UNAUTHENTICATED.getMessage());
        }
        String accessToken = jwtService.generateAccessToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        String roleName = user.getRole() != null ? user.getRole().getRoleName() : null;

        return UserResponse.UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .roleName(roleName)
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        String accessToken = logoutRequest.getAccessToken();
        String refreshToken = logoutRequest.getRefreshToken();

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        SignedJWT signedJWT = jwtService.verifyToken(accessToken, false);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        long ttl = (expiryTime.getTime() - System.currentTimeMillis()) / 1000;
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        redisService.save(jwtId, accessToken, ttl, TimeUnit.SECONDS);

        refreshTokenRepository.delete(refreshTokenEntity);
    }

    @Override
    public UserResponse.UserLoginResponse outboundAuthentication(String code) {
        // Gửi yêu cầu lấy access token từ Google (qua OutboundAuthClient)
        ExchangeTokenRequest request = ExchangeTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(GRANT_TYPE)
                .build();
        ExchangeTokenResponse response = outboundAuthClient.exchangeToken(request);

        // Gửi yêu cầu lấy thông tin user từ Google (qua OutboundUserClient)
        var userInfo = outboundUserClient.getUserInfor(RESPONSE_FORMAT, response.getAccessToken());

        UserEntity userEntity = userRepository.findByEmail(userInfo.getEmail())
                .map(existing -> {
                    if (existing.isDeleted()) {
                        throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
                    }
                    if (existing.getStatus() == UserStatus.LOCKED) {
                        throw new ResourceConflictException(ErrorCode.USER_LOCKED.getMessage());
                    }
                    return existing;
                }).orElseGet(() -> {
                    RoleEntity roleEntity = roleService.findByRoleName(PredefinedRole.ROLE_STUDENT);
                    return userRepository.save(UserEntity.builder()
                            .email(userInfo.getEmail())
                            .fullName(userInfo.getFamilyName() + " " + userInfo.getGivenName())
                            .passwordHash(passwordEncoder.encode(generateRandomPassword()))
                            .status(UserStatus.ACTIVE)
                            .provider(Provider.GOOGLE)
                            .avatarUrl(userInfo.getPicture())
                            .role(roleEntity)
                            .build());
                });

        String accessToken = jwtService.generateAccessToken(userEntity);
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(userEntity);

        String roleName = userEntity.getRole() != null ? userEntity.getRole().getRoleName() : null;

        return UserResponse.UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenEntity.getToken()) // lấy từ DB
                .roleName(roleName)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request.getToken());
    }
    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        String token = introspectRequest.getToken();
        boolean isValid = true;

        try {
            var signedJWT = jwtService.verifyToken(token, false);

            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            String blacklistedToken = redisService.get(jti);
            if (blacklistedToken != null) {
                isValid = false;
            }
        } catch (UnauthorizedException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }



    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}