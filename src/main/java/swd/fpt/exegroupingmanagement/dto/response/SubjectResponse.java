package swd.fpt.exegroupingmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponse {
    Long subjectId;
    String code;
    String name;
    String prerequisiteCodes;
    Boolean isDeleted;
}

