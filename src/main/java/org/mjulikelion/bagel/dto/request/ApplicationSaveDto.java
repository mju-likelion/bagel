package org.mjulikelion.bagel.dto.request;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_EMAIL_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_LINK_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_NAME_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_PHONE_NUMBER_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Map;
import lombok.Getter;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.util.annotaion.enumconstraint.EnumConstraint;
import org.mjulikelion.bagel.util.annotaion.grade.GradeConstraint;


@Getter
public class ApplicationSaveDto {
    @NotNull(message = "학번이 누락되었습니다.")
    @Pattern(regexp = APPLICATION_STUDENT_ID_PATTERN, message = "학번이 형식에 맞지 않습니다.")
    private String studentId;//학번

    @NotNull(message = "이름이 누락되었습니다.")
    @Pattern(regexp = APPLICATION_NAME_PATTERN, message = "이름이 형식에 맞지 않습니다.")
    private String name;//이름

    @NotBlank(message = "학과 id가 누락되었습니다.")
    private String majorId;//학과 id

    @NotNull(message = "전화번호가 누락되었습니다.")
    @Pattern(regexp = APPLICATION_PHONE_NUMBER_PATTERN, message = "전화번호가 형식에 맞지 않습니다.")
    private String phoneNumber;//전화번호

    @NotNull(message = "이메일이 누락되었습니다.")
    @Pattern(regexp = APPLICATION_EMAIL_PATTERN, message = "이메일이 형식에 맞지 않습니다.")
    private String email;//이메일

    @NotNull(message = "학년이 누락되었습니다.")
    @GradeConstraint(message = "학년이 형식에 맞지 않습니다.")
    private String grade;//학년

    @NotNull(message = "파트가 누락되었습니다.")
    @EnumConstraint(value = Part.class, message = "파트가 형식에 맞지 않습니다.")
    private String part;//파트

    @NotBlank(message = "자기소개 페이지 혹은 GitHub 링크가 누락되었습니다.")
    @Pattern(regexp = APPLICATION_LINK_PATTERN, message = "자기소개 페이지 혹은 GitHub 링크가 형식에 맞지 않습니다.")
    private String link;//자기소개 페이지 or GitHub 링크

    @NotNull(message = "자기소개가 누락되었습니다.")
    private Map<String, String> introduces;//자기소개<introduceId, 내용>, 유효성 검사는 ApplicationCommandService 에서 진행

    @NotNull(message = "동의항목이 누락되었습니다.")
    private Map<String, @AssertTrue Boolean> agreements;//동의항목<agreementId, 동의여부>, 유효성 검사는 ApplicationCommandService 에서 진행
}