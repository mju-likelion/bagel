package org.mjulikelion.bagel.dto.request;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_USER_ID_PATTERN;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ApplySaveDto {
    @NotNull
    @Pattern(regexp = APPLICATION_USER_ID_PATTERN)
    private String userId;
}
