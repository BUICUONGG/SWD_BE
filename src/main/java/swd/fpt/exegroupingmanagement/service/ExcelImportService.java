package swd.fpt.exegroupingmanagement.service;

import org.springframework.web.multipart.MultipartFile;
import swd.fpt.exegroupingmanagement.dto.response.ImportResultResponse;

public interface ExcelImportService {
    ImportResultResponse importStudents(MultipartFile file);
}

