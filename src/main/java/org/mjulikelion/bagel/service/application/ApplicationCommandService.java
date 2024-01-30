package org.mjulikelion.bagel.service.application;

import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ApplicationCommandService {
    ResponseEntity<ResponseDto<Void>> saveApplication(ApplicationSaveDto applicationSaveDto);
}
