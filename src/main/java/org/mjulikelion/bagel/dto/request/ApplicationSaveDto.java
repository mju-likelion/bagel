package org.mjulikelion.bagel.dto.request;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_NAME_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_PHONE_NUMBER_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;
import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_USER_ID_PATTERN;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Map;
import lombok.Getter;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.util.annotaion.enumconstraint.EnumConstraint;


@Getter
public class ApplicationSaveDto {

    @JsonProperty
    @NotNull
    @Pattern(regexp = APPLICATION_USER_ID_PATTERN, message = "1")
    private String userId;//지원 아이디

    @JsonProperty
    @NotNull
    @Pattern(regexp = APPLICATION_NAME_PATTERN, message = "2")
    private String name;//이름

    @JsonProperty
    @NotNull
    private String majorId;//학과 id

    @JsonProperty
    @NotNull
    @Pattern(regexp = APPLICATION_STUDENT_ID_PATTERN, message = "3")
    private String studentId;//학번

    @JsonProperty
    @NotNull
    @Pattern(regexp = APPLICATION_PHONE_NUMBER_PATTERN, message = "4")
    private String phoneNumber;//전화번호

    @JsonProperty
    @NotNull
    @Email
    @NotBlank(message = "5")
    private String email;//이메일

    @JsonProperty
    @NotNull
    @Min(1)
    @Max(5)
    private byte grade;//학년

    @JsonProperty
    @NotNull
    @EnumConstraint(Part.class)
    private Part part;//파트

    @JsonProperty
    @NotNull
    private String link;//자기소개 페이지 or GitHub 링크

    @JsonProperty
    @NotNull
    private Map<String, String> introduces;//자기소개<introduceId, 내용>, 유효성 검사는 ApplicationCommandService 에서 진행

    @JsonProperty
    @NotNull
    private Map<String, @AssertTrue Boolean> agreements;//동의항목<agreementId, 동의여부>, 유효성 검사는 ApplicationCommandService 에서 진행
}