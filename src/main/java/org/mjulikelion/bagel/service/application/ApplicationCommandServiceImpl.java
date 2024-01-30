package org.mjulikelion.bagel.service.application;

import java.util.List;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<Void>> saveApplication(ApplicationSaveDto applicationSaveDto) {
        if (this.applicationRepository.existsByUserId(applicationSaveDto.getUserId())) {
            return new ResponseEntity<>(ResponseDto.res(
                    HttpStatus.CONFLICT,
                    "Already exists"
            ), HttpStatus.CONFLICT);
        }
        Major major = this.majorRepository.findById(applicationSaveDto.getMajorId()).orElseThrow();

        //지원서 생성
        Application application = this.buildApplicationFromDto(applicationSaveDto, major);

        //지원서 동의 항목 생성
        List<ApplicationAgreement> agreements = this.applicationAgreementConvertor.convertMapToApplicationAgreement(
                applicationSaveDto.getAgreements(),
                application
        );

        //지원서 자기소개 생성
        List<ApplicationIntroduce> introduces = this.applicationIntroduceConvertor.convertMapToApplicationIntroduce(
                applicationSaveDto.getIntroduces(),
                application
        );

        //지원서 동의 항목 유효성 검사
        if (!this.isValidApplicationAgreement(agreements)) {
            return new ResponseEntity<>(ResponseDto.res(
                    HttpStatus.BAD_REQUEST,
                    "Invalid agreement"
            ), HttpStatus.BAD_REQUEST);
        }

        //지원서 자기소개 유효성 검사
        if (!this.isValidApplicationIntroduce(introduces, application.getPart())) {
            return new ResponseEntity<>(ResponseDto.res(
                    HttpStatus.BAD_REQUEST,
                    "Invalid introduce"
            ), HttpStatus.BAD_REQUEST);
        }

        //지원서, 동의 항목, 자기소개 저장
        this.saveApplicationWithDetails(application, agreements, introduces);

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED,
                "Success"
        ), HttpStatus.CREATED);
    }

    /**
     * Description: 지원서 생성
     *
     * @param dto   지원서 생성 요청 DTO
     * @param major 지원서 전공
     * @return 지원서
     */
    private Application buildApplicationFromDto(ApplicationSaveDto dto, Major major) {
        return Application.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .part(dto.getPart())
                .link(dto.getLink())
                .grade(dto.getGrade())
                .studentId(dto.getStudentId())
                .userId(dto.getUserId())
                .major(major)
                .build();
    }

    /**
     * Description: 지원서, 동의 항목, 자기소개 저장
     *
     * @param application 지원서
     * @param agreements  지원서 동의 항목
     * @param introduces  지원서 자기소개
     */
    private void saveApplicationWithDetails(Application application,
                                            List<ApplicationAgreement> agreements,
                                            List<ApplicationIntroduce> introduces) {
        applicationRepository.save(application);
        applicationAgreementRepository.saveAll(agreements);
        applicationIntroduceRepository.saveAll(introduces);
    }

    /**
     * Description: 지원서 자기소개 유효성 검사
     *
     * @param introduce 지원서 자기소개
     * @param part      지원 파트
     * @return 유효성 검사 결과
     */
    private boolean isValidApplicationIntroduce(List<ApplicationIntroduce> introduce, Part part) {
        return this.introduceRepository.countByPart(part) == introduce.size();
    }

    /**
     * Description: 지원서 동의 항목 유효성 검사
     *
     * @param agreement 지원서 동의 항목
     * @return 유효성 검사 결과
     */
    private boolean isValidApplicationAgreement(List<ApplicationAgreement> agreement) {
        return this.agreementRepository.count() == agreement.size();
    }
}
