package swd.fpt.exegroupingmanagement.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.dto.request.*;
import swd.fpt.exegroupingmanagement.dto.response.IntrospectResponse;
import swd.fpt.exegroupingmanagement.dto.response.RefreshTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<StandardResponse<Object>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authenticationService.register(request);
        return  ResponseEntity.ok(success("Đăng ký thành công", user));

    }
    @PostMapping("/login")
    public ResponseEntity<StandardResponse<Object>> login(@RequestBody @Valid UserLoginRequest userRequest) {
        UserResponse.UserLoginResponse result = authenticationService.login(userRequest);
        return ResponseEntity.ok(success("Đăng nhập thành công", result));

    }

    @PostMapping("/introspect")
    public ResponseEntity<StandardResponse<Object>> introspect(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(introspectRequest);
        return ResponseEntity.ok(success("Kiểm tra token thành công", result));

    }
    @PostMapping("/logout")
    public ResponseEntity<StandardResponse<String>> logout(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ResponseEntity.ok(success("Đăng xuất thành công"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<StandardResponse<Object>> refresh(@RequestBody RefreshTokenRequest refreshRequest) throws ParseException, JOSEException {
        RefreshTokenResponse result = authenticationService.refreshToken(refreshRequest);
        return ResponseEntity.ok(success("Làm mới token thành công", result));
    }
    @PostMapping("/login-google")
    public ResponseEntity<StandardResponse<Object>> loginGoogle(@RequestParam("code") String code){
        UserResponse.UserLoginResponse result = authenticationService.outboundAuthentication(code);
        return ResponseEntity.ok(success("Kiểm tra token bằng google thành công", result));
    }
}



