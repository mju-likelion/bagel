package org.mjulikelion.bagel.service.application;

import static org.mjulikelion.bagel.errorcode.ErrorCode.APPLICATION_ALREADY_EXISTS_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.FILE_STORAGE_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_AGREEMENT_MISSING_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_INTRODUCE_MISSING_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_MAJOR_ERROR;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.FileSaveResponseData;
import org.mjulikelion.bagel.exception.ApplicationAlreadyExistException;
import org.mjulikelion.bagel.exception.FileStorageException;
import org.mjulikelion.bagel.exception.InvalidDataException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
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
        if (applicationAlreadyExists(applicationSaveDto.getStudentId())) {
            throw new ApplicationAlreadyExistException(APPLICATION_ALREADY_EXISTS_ERROR);
        }

        Major major = this.findMajorById(applicationSaveDto.getMajorId());

        Application application = buildApplicationFromDto(applicationSaveDto, major);
        List<ApplicationAgreement> agreements = convertAgreements(applicationSaveDto, application);
        List<ApplicationIntroduce> introduces = convertIntroduces(applicationSaveDto, application);

        saveApplicationWithDetails(application, agreements, introduces);

        this.slackService.sendSlackMessage(new SlackApplySaveMessage(application));

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
     * 학생 아이디에 해당하는 지원서가 이미 존재하는지 확인.
     *
     * @param studentId 학번
     * @return 존재 여부
     */
    private boolean applicationAlreadyExists(String studentId) {
        return this.applicationRepository.existsByStudentId(studentId);
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

        if (!this.isValidAgreementsSize(agreements)) {
            throw new InvalidDataException(INVALID_AGREEMENT_MISSING_ERROR);
        }

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

        if (!this.isValidIntroducesSize(introduces, application.getPart())) {
            throw new InvalidDataException(INVALID_INTRODUCE_MISSING_ERROR);
        }

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
     * 지원서, 동의 항목, 자기소개를 저장.
     *
     * @param application 지원서
     * @param agreements  지원서 동의 항목
     * @param introduces  지원서 자기소개
     */
    private void saveApplicationWithDetails(Application application, List<ApplicationAgreement> agreements,
                                            List<ApplicationIntroduce> introduces) {

        applicationRepository.save(application);
        applicationAgreementRepository.saveAll(agreements);
        applicationIntroduceRepository.saveAll(introduces);
    }
}
