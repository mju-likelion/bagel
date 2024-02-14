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
    String message() default "Invalid Introduce file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
