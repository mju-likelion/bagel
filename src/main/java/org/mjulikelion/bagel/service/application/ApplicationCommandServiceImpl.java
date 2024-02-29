package org.mjulikelion.bagel.service.application;

import static org.mjulikelion.bagel.errorcode.ErrorCode.APPLICATION_ALREADY_EXISTS_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.FILE_STORAGE_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_AGREEMENT_MISSING_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_INTRODUCE_LENGTH_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_INTRODUCE_MISSING_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_MAJOR_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.JPA_ERROR;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.FileSaveResponseData;
import org.mjulikelion.bagel.exception.ApplicationAlreadyExistException;
import org.mjulikelion.bagel.exception.FileStorageException;
import org.mjulikelion.bagel.exception.InvalidDataException;
import org.mjulikelion.bagel.exception.JpaException;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationAgreement;
import org.mjulikelion.bagel.model.ApplicationIntroduce;
import org.mjulikelion.bagel.model.Major;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.repository.AgreementRepository;
import org.mjulikelion.bagel.repository.ApplicationAgreementRepository;
import org.mjulikelion.bagel.repository.ApplicationIntroduceRepository;
import org.mjulikelion.bagel.repository.ApplicationRepository;
import org.mjulikelion.bagel.repository.IntroduceRepository;
import org.mjulikelion.bagel.repository.MajorRepository;
import org.mjulikelion.bagel.util.converter.ApplicationAgreementConverter;
import org.mjulikelion.bagel.util.converter.ApplicationIntroduceConverter;
import org.mjulikelion.bagel.util.s3.S3Service;
import org.mjulikelion.bagel.util.slack.SlackService;
import org.mjulikelion.bagel.util.slack.asset.message.SlackApplySaveMessage;
import org.mjulikelion.bagel.util.slack.asset.message.SlackFileMessage;
import org.mjulikelion.bagel.vo.ApplicationDetailsVO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ApplicationCommandServiceImpl implements ApplicationCommandService {
    private final ApplicationRepository applicationRepository;
    private final MajorRepository majorRepository;
    private final IntroduceRepository introduceRepository;
    private final AgreementRepository agreementRepository;
    private final ApplicationAgreementRepository applicationAgreementRepository;
    private final ApplicationIntroduceRepository applicationIntroduceRepository;
    private final ApplicationAgreementConverter applicationAgreementConvertor;
    private final ApplicationIntroduceConverter applicationIntroduceConvertor;
    private final S3Service s3Service;
    private final SlackService slackService;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<Void>> saveApplication(ApplicationSaveDto applicationSaveDto) {
        String majorId = applicationSaveDto.getMajorId();
        Major major = this.findMajorById(majorId);

        ApplicationDetailsVO applicationDetailsVO = this.buildApplicationDetailsVO(applicationSaveDto, major);
        this.saveApplicationDetailsVO(applicationDetailsVO);
        this.slackService.sendSlackMessage(new SlackApplySaveMessage(applicationDetailsVO.application()));

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "Created"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDto<FileSaveResponseData>> saveFile(MultipartFile file) {
        try {
            String url = this.s3Service.saveFile(file);
            this.slackService.sendSlackMessage(new SlackFileMessage(file.getOriginalFilename(), url));
            return new ResponseEntity<>(
                    ResponseDto.res(HttpStatus.CREATED, "Created", new FileSaveResponseData(url)),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            throw new FileStorageException(FILE_STORAGE_ERROR, e.getMessage());
        }
    }

    /**
     * 지원서 DTO와 Major를 사용하여 ApplicationDetails를 생성.
     *
     * @param applicationSaveDto 지원서 DTO
     * @param major              지원서의 Major
     * @return 생성된 ApplicationDetails 객체
     */
    private ApplicationDetailsVO buildApplicationDetailsVO(ApplicationSaveDto applicationSaveDto, Major major) {
        try {
            Application application = buildApplicationFromDto(applicationSaveDto, major);
            List<ApplicationAgreement> agreements = convertAgreements(applicationSaveDto, application);
            List<ApplicationIntroduce> introduces = convertIntroduces(applicationSaveDto, application);
            return new ApplicationDetailsVO(application, agreements, introduces);
        } catch (EntityExistsException | DataIntegrityViolationException e) {
            throw new ApplicationAlreadyExistException(APPLICATION_ALREADY_EXISTS_ERROR);
        } catch (JpaSystemException e) {
            throw new JpaException(JPA_ERROR, e.getMessage());
        }
    }

    /**
     * ApplicationDetails를 DB에 저장.
     *
     * @param applicationDetailsVO ApplicationDetails 객체
     */
    private void saveApplicationDetailsVO(ApplicationDetailsVO applicationDetailsVO) {
        try {
            this.applicationRepository.saveAndFlush(applicationDetailsVO.application());
            this.applicationAgreementRepository.saveAll(applicationDetailsVO.agreements());
            this.applicationIntroduceRepository.saveAll(applicationDetailsVO.introduces());
        } catch (EntityExistsException | DataIntegrityViolationException e) {
            throw new ApplicationAlreadyExistException(APPLICATION_ALREADY_EXISTS_ERROR);
        } catch (JpaSystemException e) {
            throw new JpaException(JPA_ERROR, e.getMessage());
        }
    }

    /**
     * ID를 기반으로 Major를 찾아 반환.
     *
     * @param majorId Major의 ID
     * @return 찾아진 Major 객체의 Optional
     */
    private Major findMajorById(String majorId) {
        return this.majorRepository.findById(majorId).orElseThrow(() -> new InvalidDataException(INVALID_MAJOR_ERROR));
    }

    /**
     * 지원서 DTO와 Major를 사용하여 지원서를 생성.
     *
     * @param applicationSaveDto 지원서 DTO
     * @param major              지원서의 Major
     * @return 생성된 지원서 객체
     */
    private Application buildApplicationFromDto(ApplicationSaveDto applicationSaveDto, Major major) {
        return Application.builder()
                .name(applicationSaveDto.getName())
                .phoneNumber(applicationSaveDto.getPhoneNumber())
                .email(applicationSaveDto.getEmail())
                .part(Part.findBy(applicationSaveDto.getPart()))
                .link(applicationSaveDto.getLink())
                .grade(applicationSaveDto.getGrade())
                .studentId(applicationSaveDto.getStudentId())
                .major(major)
                .build();
    }

    /**
     * 지원서 동의 항목을 변환하고 유효성 검사를 수행.
     *
     * @param applicationSaveDto 지원서 DTO
     * @param application        변환 대상 지원서
     * @return 유효한 경우 변환된 동의 항목 리스트, 그렇지 않은 경우 null
     */
    private List<ApplicationAgreement> convertAgreements(ApplicationSaveDto applicationSaveDto,
                                                         Application application) {
        List<ApplicationAgreement> agreements = this.applicationAgreementConvertor.convertMapToApplicationAgreement(
                applicationSaveDto.getAgreements(), application);

        this.validateAgreements(agreements);

        return agreements;
    }

    /**
     * 지원서 자기소개를 변환하고 유효성 검사를 수행.
     *
     * @param applicationSaveDto 지원서 DTO
     * @param application        변환 대상 지원서
     * @return 유효한 경우 변환된 자기소개 리스트, 그렇지 않은 경우 null
     */
    private List<ApplicationIntroduce> convertIntroduces(ApplicationSaveDto applicationSaveDto,
                                                         Application application) {
        List<ApplicationIntroduce> introduces = this.applicationIntroduceConvertor.convertMapToApplicationIntroduce(
                applicationSaveDto.getIntroduces(), application);

        this.validateIntroduces(introduces, application.getPart());

        return introduces;
    }

    /**
     * 지원서 동의 항목 리스트의 누락 여부를 검사.
     *
     * @param agreements 지원서 동의 항목 리스트
     * @return 유효한 경우 true, 그렇지 않은 경우 false
     */
    private boolean isValidAgreementsSize(List<ApplicationAgreement> agreements) {
        return this.agreementRepository.count() == agreements.size();
    }

    private void validateAgreements(List<ApplicationAgreement> agreements) {
        if (!isValidAgreementsSize(agreements)) {
            throw new InvalidDataException(INVALID_AGREEMENT_MISSING_ERROR);
        }
    }

    /**
     * 지원서 자기소개 리스트의 누락 여부를 검사.
     *
     * @param introduces 지원서 자기소개 리스트
     * @param part       지원 파트
     * @return 유효한 경우 true, 그렇지 않은 경우 false
     */
    private boolean isValidIntroducesSize(List<ApplicationIntroduce> introduces, Part part) {
        return introduces.size() == this.introduceRepository.countByPart(part);
    }

    /**
     * 지원서 자기소개의 글자수를 검사.
     *
     * @param introduces 지원서 자기소개 리스트
     * @param part       지원 파트
     * @return 유효한 경우 true, 그렇지 않은 경우 false
     */
    private boolean isValidIntroducesLength(List<ApplicationIntroduce> introduces, Part part) {
        //introduce의 아이디를 DB에 있는 introduce의 아이디와 비교해서 글자수가 맞는지 확인
        return IntStream.range(0, introduces.size())
                .allMatch(i -> introduces.get(i).getContent().length() <= this.introduceRepository.findById(
                        introduces.get(i).getIntroduce().getId()).orElseThrow().getMaxLength());
    }

    /**
     * 지원서 자기소개의 유효성을 검사.
     *
     * @param introduces 지원서 자기소개 리스트
     * @param part       지원 파트
     */
    private void validateIntroduces(List<ApplicationIntroduce> introduces, Part part) {
        if (!isValidIntroducesSize(introduces, part)) {
            throw new InvalidDataException(INVALID_INTRODUCE_MISSING_ERROR);
        }
        if (!isValidIntroducesLength(introduces, part)) {
            throw new InvalidDataException(INVALID_INTRODUCE_LENGTH_ERROR);
        }
    }
}
