package swd.fpt.exegroupingmanagement.service;

import com.nimbusds.jose.JOSEException;
import swd.fpt.exegroupingmanagement.dto.request.*;
import swd.fpt.exegroupingmanagement.dto.response.IntrospectResponse;
import swd.fpt.exegroupingmanagement.dto.response.RefreshTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;

    UserResponse.UserLoginResponse login(UserLoginRequest accountLoginRequest);

    UserResponse register(RegisterRequest registerRequest);

    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

    UserResponse.UserLoginResponse outboundAuthentication(String code);
}