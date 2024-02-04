package org.mjulikelion.bagel.service.apply;

import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.repository.ApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ApplyCommandServiceImpl implements ApplyCommandService {
    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<Void>> saveApply(ApplySaveDto applySaveDto) {
        if (this.applicationRepository.existsByUserId(applySaveDto.getUserId())) {
            return new ResponseEntity<>(ResponseDto.res(
                    HttpStatus.CONFLICT,
                    "Already applied"
            ), HttpStatus.CONFLICT);
        }

        Application application = Application.builder()
                .userId(applySaveDto.getUserId())
                .build();
        this.applicationRepository.save(application);
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED,
                "Created"
        ), HttpStatus.CREATED);
    }
}
