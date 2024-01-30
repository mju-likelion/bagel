package org.mjulikelion.bagel.util.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationIntroduce;
import org.mjulikelion.bagel.repository.IntroduceRepository;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ApplicationIntroduceConverter {
    private final IntroduceRepository introduceRepository;

    /**
     * Description: {@code Map<String, String>, Application -> List<ApplicationIntroduce> 변환}
     *
     * @param introduceMap introduce 아이디, introduce 내용
     * @param application  application 객체
     * @return ApplicationIntroduce 리스트
     */
    public List<ApplicationIntroduce> convertMapToApplicationIntroduce(Map<String, String> introduceMap,
                                                                       Application application) {
        return introduceMap.keySet().stream()
                .map(introduce -> ApplicationIntroduce.builder()
                        .introduce(this.introduceRepository.findById(introduce).orElseThrow())
                        .content(introduceMap.get(introduce))
                        .application(application)
                        .build())
                .collect(Collectors.toList());
    }
}
