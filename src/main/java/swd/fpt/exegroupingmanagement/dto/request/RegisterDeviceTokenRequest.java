package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.DeviceType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDeviceTokenRequest {
    @NotBlank(message = "Device token không được để trống")
    String token;

    @NotNull(message = "Device type không được để trống")
    DeviceType deviceType;
    
    String deviceName;
}

