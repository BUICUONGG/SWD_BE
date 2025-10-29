package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportResultResponse {
    Integer totalRows;
    Integer successCount;
    Integer failureCount;
    List<ImportError> errors;
    
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ImportError {
        Integer rowNumber;
        String email;
        String errorMessage;
    }
}

