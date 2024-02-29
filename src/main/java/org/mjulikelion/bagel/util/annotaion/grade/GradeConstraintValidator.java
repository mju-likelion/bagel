package org.mjulikelion.bagel.util.annotaion.grade;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_GRADE_PATTERN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class GradeConstraintValidator implements ConstraintValidator<GradeConstraint, String> {
    @Override
    public void initialize(GradeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(APPLICATION_GRADE_PATTERN);
        return value != null && pattern.matcher(value).matches();
    }
}
