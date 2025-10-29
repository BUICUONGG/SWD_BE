package swd.fpt.exegroupingmanagement.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterRequest {
    
    @NotBlank(message = "Mã kỳ học không được để trống")
    @Size(max = 20, message = "Mã kỳ học không được quá 20 ký tự")
    String code;
    
    @NotBlank(message = "Tên kỳ học không được để trống")
    @Size(max = 100, message = "Tên kỳ học không được quá 100 ký tự")
    String name;
    
    @NotNull(message = "Năm không được để trống")
    Integer year;
    
    @NotNull(message = "Học kỳ không được để trống")
    SemesterTerm term;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    LocalDate startDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    LocalDate endDate;
}

