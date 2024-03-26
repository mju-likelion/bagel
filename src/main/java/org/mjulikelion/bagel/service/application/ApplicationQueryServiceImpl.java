package org.mjulikelion.bagel.service.application;

import java.util.List;
import java.util.Optional;
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
import org.mjulikelion.bagel.util.redis.RedisUtilFactory;
import org.mjulikelion.bagel.util.redis.StringRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final IntroduceRepository introduceRepository;
    private final AgreementRepository agreementRepository;
    private final MajorRepository majorRepository;
    private final StringRedisUtil<ApplicationGetResponseData> applicationGetResponseDataStringRedisUtil;

    @Autowired
    public ApplicationQueryServiceImpl(IntroduceRepository introduceRepository,
                                       AgreementRepository agreementRepository,
                                       MajorRepository majorRepository,
                                       RedisUtilFactory redisUtilFactory) {
        this.introduceRepository = introduceRepository;
        this.agreementRepository = agreementRepository;
        this.majorRepository = majorRepository;
        this.applicationGetResponseDataStringRedisUtil = redisUtilFactory.createStringRedisUtil(
                ApplicationGetResponseData.class);
    }

    @Override
    public ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(String part) {
        Part partEnum = Part.findBy(part.toUpperCase());

        Optional<ApplicationGetResponseData> cachedResponse = this.getCachedApplicationGetResponseData(partEnum);

        ApplicationGetResponseData applicationGetResponseData = cachedResponse.orElseGet(() -> {
            ApplicationGetResponseData responseData = this.buildApplicationGetResponseData(partEnum);
            this.cacheApplicationGetResponseData(partEnum, responseData);
            return responseData;
        });

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.OK,
                "Success",
                applicationGetResponseData
        ), HttpStatus.OK);
    }

    /**
     * ApplicationGetResponseData를 Redis에 캐싱한다.
     *
     * @param partEnum                   part
     * @param applicationGetResponseData ApplicationGetResponseData
     */
    private void cacheApplicationGetResponseData(Part partEnum, ApplicationGetResponseData applicationGetResponseData) {
        this.applicationGetResponseDataStringRedisUtil.insert(partEnum.name(),
                applicationGetResponseData);
    }

    /**
     * Redis에서 ApplicationGetResponseData를 가져온다.
     *
     * @param partEnum part
     * @return {@code Optional<ApplicationGetResponseData>}
     */
    private Optional<ApplicationGetResponseData> getCachedApplicationGetResponseData(Part partEnum) {
        return Optional.ofNullable(applicationGetResponseDataStringRedisUtil.select(partEnum.name()));
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
}