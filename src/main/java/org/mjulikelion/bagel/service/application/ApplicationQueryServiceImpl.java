package org.mjulikelion.bagel.service.application;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.mjulikelion.bagel.model.Agreement;
import org.mjulikelion.bagel.model.Introduce;
import org.mjulikelion.bagel.model.Major;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.repository.AgreementRepository;
import org.mjulikelion.bagel.repository.IntroduceRepository;
import org.mjulikelion.bagel.repository.MajorRepository;
import org.mjulikelion.bagel.util.redis.ApplicationGetResponseDataRedisUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final IntroduceRepository introduceRepository;
    private final AgreementRepository agreementRepository;
    private final MajorRepository majorRepository;
    private final ApplicationGetResponseDataRedisUtil applicationGetResponseDataRedisUtil;

    @Override
    public ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(String part) {
        Part partEnum = Part.findBy(part.toUpperCase());

        if (partEnum == null) {
            return this.handleInvalidPart();
        }

        Optional<ApplicationGetResponseData> cachedResponse = this.getCachedApplicationGetResponseData(partEnum);

        ApplicationGetResponseData response = cachedResponse.orElseGet(() -> {
            ApplicationGetResponseData responseData = this.buildApplicationGetResponseData(partEnum);
            this.cacheApplicationGetResponseData(partEnum, responseData);
            return responseData;
        });

        return this.successResponse(response);
    }

    /**
     * part가 유효하지 않은 경우의 ResponseEntity를 생성하여 반환.
     *
     * @return part가 유효하지 않은 경우의 ResponseEntity
     */
    private ResponseEntity<ResponseDto<ApplicationGetResponseData>> handleInvalidPart() {
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.BAD_REQUEST,
                "Invalid part"
        ), HttpStatus.BAD_REQUEST);
    }

    /**
     * ApplicationGetResponseData를 Redis에 캐싱한다.
     *
     * @param partEnum                   part
     * @param applicationGetResponseData ApplicationGetResponseData
     */
    private void cacheApplicationGetResponseData(Part partEnum, ApplicationGetResponseData applicationGetResponseData) {
        this.applicationGetResponseDataRedisUtil.setApplicationGetResponseData(partEnum.name(),
                applicationGetResponseData);
    }

    /**
     * Redis에서 ApplicationGetResponseData를 가져온다.
     *
     * @param partEnum part
     * @return {@code Optional<ApplicationGetResponseData>}
     */
    private Optional<ApplicationGetResponseData> getCachedApplicationGetResponseData(Part partEnum) {
        return Optional.ofNullable(applicationGetResponseDataRedisUtil.getApplicationGetResponseData(partEnum.name()));
    }

    /**
     * ApplicationGetResponseData를 생성한다.
     *
     * @param partEnum part
     * @return ApplicationGetResponseData
     */
    private ApplicationGetResponseData buildApplicationGetResponseData(Part partEnum) {
        List<Introduce> introduces = introduceRepository.findAllByPartOrderBySequence(partEnum);
        List<Agreement> agreements = agreementRepository.findAllByOrderBySequence();
        List<Major> majors = majorRepository.findAllByOrderBySequence();

        return ApplicationGetResponseData.builder()
                .introduces(introduces)
                .agreements(agreements)
                .majors(majors)
                .build();
    }

    /**
     * 지원서 저장이 성공한 경우의 ResponseEntity를 생성하여 반환.
     *
     * @return 지원서 저장이 성공한 경우의 ResponseEntity
     */
    private ResponseEntity<ResponseDto<ApplicationGetResponseData>> successResponse(
            ApplicationGetResponseData applicationGetResponseData) {
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.OK,
                "Success",
                applicationGetResponseData
        ), HttpStatus.OK);
    }
}