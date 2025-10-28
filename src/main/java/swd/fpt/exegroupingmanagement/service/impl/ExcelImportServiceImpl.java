package swd.fpt.exegroupingmanagement.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.dto.request.StudentImportDto;
import swd.fpt.exegroupingmanagement.dto.response.ImportResultResponse;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.BusinessException;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.ExcelImportService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExcelImportServiceImpl implements ExcelImportService {
    
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public ImportResultResponse importStudents(MultipartFile file) {
        List<ImportResultResponse.ImportError> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;
        int totalRows = 0;
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                totalRows++;
                
                try {
                    StudentImportDto dto = parseRow(row, i + 1);
                    if (dto != null) {
                        createStudent(dto);
                        successCount++;
                    }
                } catch (Exception e) {
                    failureCount++;
                    errors.add(ImportResultResponse.ImportError.builder()
                            .rowNumber(i + 1)
                            .email(getCellValueAsString(row.getCell(0)))
                            .errorMessage(e.getMessage())
                            .build());
                    log.error("Error importing row {}: {}", i + 1, e.getMessage());
                }
            }
            
        } catch (IOException e) {
            throw new BusinessException("Không thể đọc file Excel: " + e.getMessage());
        }
        
        return ImportResultResponse.builder()
                .totalRows(totalRows)
                .successCount(successCount)
                .failureCount(failureCount)
                .errors(errors)
                .build();
    }
    
    private StudentImportDto parseRow(Row row, int rowNumber) {
        // Column order: Email | Full Name | Password | Gender | Date of Birth
        
        String email = getCellValueAsString(row.getCell(0));
        String fullName = getCellValueAsString(row.getCell(1));
        String password = getCellValueAsString(row.getCell(2));
        String genderStr = getCellValueAsString(row.getCell(3));
        LocalDate dob = getCellValueAsDate(row.getCell(4));
        
        // Validate required fields
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("Email không được để trống");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new BusinessException("Họ tên không được để trống");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("Mật khẩu không được để trống");
        }
        
        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@fpt\\.edu\\.vn$")) {
            throw new BusinessException("Email phải có định dạng @fpt.edu.vn");
        }
        
        // Parse gender
        Gender gender = parseGender(genderStr);
        
        // Get STUDENT role (assuming role_id = 2 or find by name)
        RoleEntity studentRole = roleRepository.findById(2L)
                .orElseThrow(() -> new BusinessException("Không tìm thấy role STUDENT"));
        
        return StudentImportDto.builder()
                .email(email.trim())
                .fullName(fullName.trim())
                .password(password.trim())
                .gender(gender)
                .dob(dob)
                .roleId(studentRole.getRoleId())
                .build();
    }
    
    private void createStudent(StudentImportDto dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email đã tồn tại: " + dto.getEmail());
        }
        
        // Get role
        RoleEntity role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy role"));
        
        // Create user
        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .gender(dto.getGender())
                .dob(dto.getDob())
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
        
        userRepository.save(user);
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }
    
    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) return null;
        
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                // Try to parse string as date (format: yyyy-MM-dd or dd/MM/yyyy)
                String dateStr = cell.getStringCellValue();
                return LocalDate.parse(dateStr);
            }
        } catch (Exception e) {
            log.warn("Cannot parse date from cell: {}", e.getMessage());
        }
        
        return null;
    }
    
    private Gender parseGender(String genderStr) {
        if (genderStr == null || genderStr.trim().isEmpty()) {
            return Gender.FEMALE; // Default
        }
        
        String normalized = genderStr.trim().toUpperCase();
        return switch (normalized) {
            case "MALE", "NAM", "M" -> Gender.MALE;
            case "FEMALE", "NỮ", "NU", "F" -> Gender.FEMALE;
            default -> Gender.FEMALE; // Default to FEMALE if unknown
        };
    }
}

