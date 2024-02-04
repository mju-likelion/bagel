package org.mjulikelion.bagel.service.application;

import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.mjulikelion.bagel.model.Part;
import org.springframework.http.ResponseEntity;

public interface ApplicationQueryService {
    ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(Part part);
}
