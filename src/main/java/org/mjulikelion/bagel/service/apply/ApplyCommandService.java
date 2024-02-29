package org.mjulikelion.bagel.service.apply;

import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ApplyCommandService {
    ResponseEntity<ResponseDto<Void>> saveApply(ApplySaveDto applySaveDto);
}
