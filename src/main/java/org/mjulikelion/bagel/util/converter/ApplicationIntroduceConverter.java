package org.mjulikelion.bagel.util.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationIntroduce;
import org.mjulikelion.bagel.repository.IntroduceRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ApplicationIntroduceConverter {
    private final IntroduceRepository introduceRepository;

    /**
     * {@code Map<String, String>, Application -> List<ApplicationIntroduce> 변환}
     *
     * @param introduceMap introduce 아이디, introduce 내용
     * @param application  application 객체
     * @return ApplicationIntroduce 리스트
     */
    public List<ApplicationIntroduce> convertMapToApplicationIntroduce(Map<String, String> introduceMap,
                                                                       Application application) {
        List<String> introduceList = new ArrayList<>(introduceMap.keySet());
        if (!this.isValidIntroduceMap(introduceList)) {
            return null;
        }
        return introduceList.stream()
                .map(introduce -> ApplicationIntroduce.builder()
                        .introduce(this.introduceRepository.findById(introduce).orElseThrow())
                        .content(introduceMap.get(introduce))
                        .application(application)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * introduceList가 유효한지 확인
     *
     * @param introduceMap introduce 아이디 리스트
     * @return 유효하면 true, 아니면 false
     */
    private boolean isValidIntroduceMap(List<String> introduceMap) {
        return introduceMap.stream()
                .allMatch(this.introduceRepository::existsById);
    }
}
