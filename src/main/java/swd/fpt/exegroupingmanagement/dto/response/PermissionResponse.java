package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    Long permissionId;
    String permissionName;
}
