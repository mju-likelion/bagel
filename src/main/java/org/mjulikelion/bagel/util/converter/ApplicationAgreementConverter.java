package org.mjulikelion.bagel.util.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationAgreement;
import org.mjulikelion.bagel.repository.AgreementRepository;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ApplicationAgreementConverter {
    private final AgreementRepository agreementRepository;

    /**
     * Description: {@code Map<String, String>, Application -> List<ApplicationAgreement> 변환}
     *
     * @param agreementMap agreement 아이디, 동의 여부
     * @param application  application 객체
     * @return ApplicationAgreement 리스트
     */
    public List<ApplicationAgreement> convertMapToApplicationAgreement(Map<String, Boolean> agreementMap,
                                                                       Application application) {
        return agreementMap.keySet().stream()
                .map(agreementId -> ApplicationAgreement.builder()
                        .agreement(agreementRepository.findById(agreementId).orElseThrow())
                        .application(application)
                        .build())
                .collect(Collectors.toList());
    }
}
