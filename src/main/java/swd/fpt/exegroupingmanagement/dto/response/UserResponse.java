package swd.fpt.exegroupingmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.Provider;

import java.time.LocalDate;

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
        String roleName;
    }
}
