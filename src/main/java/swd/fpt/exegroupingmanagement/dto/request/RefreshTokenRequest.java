package swd.fpt.exegroupingmanagement.dto.request;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String token; // Lấy từ body hoặc cookie
}
