package org.mjulikelion.bagel.util.converter;

import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_INTRODUCE_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.exception.InvalidDataException;
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
        return introduceList.stream()
                .map(introduce -> ApplicationIntroduce.builder()
                        .introduce(
                                this.introduceRepository.findById(introduce).orElseThrow(() -> new InvalidDataException(
                                        INVALID_INTRODUCE_ERROR)))
                        .content(introduceMap.get(introduce))
                        .application(application)
                        .build())
                .collect(Collectors.toList());
    }
}
