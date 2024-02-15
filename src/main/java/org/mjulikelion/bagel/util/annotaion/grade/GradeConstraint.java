package org.mjulikelion.bagel.util.annotaion.grade;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GradeConstraintValidator.class)
public @interface GradeConstraint {
    String message() default "Invalid grade";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
