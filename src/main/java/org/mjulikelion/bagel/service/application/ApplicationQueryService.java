package org.mjulikelion.bagel.service.application;

import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.springframework.http.ResponseEntity;

public interface ApplicationQueryService {
    ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(String part);
}
