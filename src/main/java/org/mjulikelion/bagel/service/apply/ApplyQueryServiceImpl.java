package org.mjulikelion.bagel.service.apply;

import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.apply.ApplyExistResponseData;
import org.mjulikelion.bagel.repository.ApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplyQueryServiceImpl implements ApplyQueryService {
    private final ApplicationRepository applicationRepository;

    @Override
    public ResponseEntity<ResponseDto<ApplyExistResponseData>> getApplyExist(String studentId) {
        ApplyExistResponseData applicationExistResponse = ApplyExistResponseData.builder()
                .isExist(this.applicationRepository.existsByStudentId(studentId))
                .build();
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.OK,
                "Success",
                applicationExistResponse
        ), HttpStatus.OK);
    }
}
