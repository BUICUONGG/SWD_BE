package swd.fpt.exegroupingmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SemesterResponse{
    Long semesterId;
    String code;
    String name;
    Integer year;
    SemesterTerm term;
    LocalDate startDate;
    LocalDate endDate;
    Boolean isDeleted;
}

