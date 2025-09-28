package swd.fpt.exegroupingmanagement.dto.request;

import lombok.Getter;

@Getter
public class LogoutRequest {
    String accessToken;
    String refreshToken;
}
