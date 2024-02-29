package org.mjulikelion.bagel.service.apply;

import static org.mjulikelion.bagel.errorcode.ErrorCode.APPLICATION_ALREADY_EXISTS_ERROR;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.exception.ApplicationAlreadyExistException;
import org.mjulikelion.bagel.model.History;
import org.mjulikelion.bagel.repository.ApplicationRepository;
import org.mjulikelion.bagel.repository.HistoryRepository;
import org.mjulikelion.bagel.util.slack.SlackService;
import org.mjulikelion.bagel.util.slack.asset.message.SlackApplyMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ApplyCommandServiceImpl implements ApplyCommandService {
    private final ApplicationRepository applicationRepository;
    private final HistoryRepository historyRepository;
    private final SlackService slackService;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<Void>> saveApply(ApplySaveDto applySaveDto) {
        String studentId = applySaveDto.getStudentId();
        if (this.applicationRepository.existsByStudentId(studentId)) {
            throw new ApplicationAlreadyExistException(APPLICATION_ALREADY_EXISTS_ERROR);
        }

        History history = History.builder().studentId(studentId).build();

        this.saveHistory(history, studentId);

        this.slackService.sendSlackMessage(new SlackApplyMessage(studentId));

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED,
                "Created"
        ), HttpStatus.CREATED);
    }

    private void saveHistory(History history, String studentId) {
        try {
            this.historyRepository.saveAndFlush(history);
        } catch (PersistenceException e) {
            log.warn("JPA System Exception while saving History: {}, studentId: {}", e.getMessage(),
                    studentId);
        } catch (Exception ignore) {
        }
    }
}
