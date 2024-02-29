package org.mjulikelion.bagel.util.annotaion.file.introduce;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntroduceFileConstraintValidator.class)
public @interface IntroduceFileConstraint {
    String message() default "파일의 형식이 잘못되었거나 유효하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
