package org.mjulikelion.bagel.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationErrorCode {
    VALIDATION("VALIDATION 9000", "유효성 검사 실패."),
    NOT_NULL("VALIDATION 9001", "필수값이 누락되었습니다."),
    NOT_BLANK("VALIDATION 9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("VALIDATION 9003", "형식에 맞지 않습니다."),
    ENUM_CONSTRAINT("VALIDATION 9004", "유효하지 않은 Enum 값입니다."),
    GRADE_CONSTRAINT("VALIDATION 9005", "학년이 형식에 맞지 않습니다.");


    private final String code;
    private final String message;

    //Dto의 어노테이션을 통해 발생한 에러코드를 반환
    public static ValidationErrorCode resolveAnnotation(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "EnumConstraint" -> ENUM_CONSTRAINT;
            case "GradeConstraint" -> GRADE_CONSTRAINT;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}

