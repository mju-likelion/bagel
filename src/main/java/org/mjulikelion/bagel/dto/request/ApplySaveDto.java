package org.mjulikelion.bagel.dto.request;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ApplySaveDto {
    @NotNull(message = "학번이 누락되었습니다.")
    @Pattern(regexp = APPLICATION_STUDENT_ID_PATTERN, message = "학번이 형식에 맞지 않습니다.")
    private String studentId;//학번
}
