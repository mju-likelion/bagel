package org.mjulikelion.bagel.service.apply;

import static org.mjulikelion.bagel.errorcode.ErrorCode.APPLICATION_ALREADY_EXISTS_ERROR;

import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.exception.ApplicationAlreadyExistException;
import org.mjulikelion.bagel.model.History;
import org.mjulikelion.bagel.repository.ApplicationRepository;
import org.mjulikelion.bagel.repository.HistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ApplyCommandServiceImpl implements ApplyCommandService {
    private final ApplicationRepository applicationRepository;
    private final HistoryRepository historyRepository;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<Void>> saveApply(ApplySaveDto applySaveDto) {
        //이미 지원서가 있는지 확인
        if (this.applicationRepository.existsByStudentId(applySaveDto.getStudentId())) {
            throw new ApplicationAlreadyExistException(APPLICATION_ALREADY_EXISTS_ERROR);
        }

        History history = History.builder()
                .studentId(applySaveDto.getStudentId())
                .build();
        this.historyRepository.save(history);

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED,
                "Created"
        ), HttpStatus.CREATED);
    }
}
