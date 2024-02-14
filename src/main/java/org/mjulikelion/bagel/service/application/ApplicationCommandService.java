package org.mjulikelion.bagel.service.application;

import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.FileSaveResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicationCommandService {
    ResponseEntity<ResponseDto<Void>> saveApplication(ApplicationSaveDto applicationSaveDto);

    ResponseEntity<ResponseDto<FileSaveResponseData>> saveFile(MultipartFile file);
}
