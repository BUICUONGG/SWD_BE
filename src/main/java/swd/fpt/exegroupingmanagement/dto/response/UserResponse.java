package swd.fpt.exegroupingmanagement.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.Provider;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends ResponseAbstract{
    Long userId;
    String email;
    String fullName;
    String avatarUrl;
    String providerId;
    Provider provider = Provider.LOCAL;
    Gender gender = Gender.FEMALE;
    LocalDate dob;
    MajorResponse major;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    public static class UserLoginResponse{
        String accessToken;
        String refreshToken;
        Long userId;
        String roleName;
    }
}
