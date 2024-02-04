package org.mjulikelion.bagel.service.apply;

import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.apply.ApplyExistResponseData;
import org.springframework.http.ResponseEntity;

public interface ApplyQueryService {
    ResponseEntity<ResponseDto<ApplyExistResponseData>> getApplyExist(String userId);
}
