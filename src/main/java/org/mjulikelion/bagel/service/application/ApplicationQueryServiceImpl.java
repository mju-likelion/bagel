package org.mjulikelion.bagel.service.application;

import java.util.List;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.mjulikelion.bagel.model.Agreement;
import org.mjulikelion.bagel.model.Introduce;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.repository.AgreementRepository;
import org.mjulikelion.bagel.repository.IntroduceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApplicationQueryServiceImpl implements ApplicationQueryService {
    private final IntroduceRepository introduceRepository;
    private final AgreementRepository agreementRepository;

    @Autowired
    public ApplicationQueryServiceImpl(IntroduceRepository introduceRepository,
                                       AgreementRepository agreementRepository) {
        this.introduceRepository = introduceRepository;
        this.agreementRepository = agreementRepository;
    }

    @Override
    public ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(Part part) {
        List<Introduce> introduces = this.introduceRepository.findAllByPartOrderBySequence(
                part);
        List<Agreement> agreements = this.agreementRepository.findAll(
                Sort.sort(Agreement.class).by(Agreement::getSequence));

        ApplicationGetResponseData applicationGetResponse = ApplicationGetResponseData.builder()
                .introduces(introduces)
                .agreements(agreements)
                .build();

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.OK,
                "Success",
                applicationGetResponse
        ), HttpStatus.OK);
    }
}
