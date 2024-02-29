package org.mjulikelion.bagel.util.converter;

import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_AGREEMENT_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.exception.InvalidDataException;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationAgreement;
import org.mjulikelion.bagel.repository.AgreementRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ApplicationAgreementConverter {
    private final AgreementRepository agreementRepository;

    /**
     * {@code Map<String, String>, Application -> List<ApplicationAgreement> 변환}.
     *
     * @param agreementMap agreement 아이디, 동의 여부
     * @param application  application 객체
     * @return ApplicationAgreement 리스트
     */
    public List<ApplicationAgreement> convertMapToApplicationAgreement(Map<String, Boolean> agreementMap,
                                                                       Application application) {
        List<String> agreementIdList = new ArrayList<>(agreementMap.keySet());
        return agreementIdList.stream()
                .map(agreementId -> ApplicationAgreement.builder()
                        .agreement(this.agreementRepository.findById(agreementId)
                                .orElseThrow(() -> new InvalidDataException(
                                        INVALID_AGREEMENT_ERROR)))
                        .application(application)
                        .build())
                .collect(Collectors.toList());
    }
}
