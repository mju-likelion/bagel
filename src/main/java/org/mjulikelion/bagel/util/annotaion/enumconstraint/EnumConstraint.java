package org.mjulikelion.bagel.util.annotaion.enumconstraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 값이 주어진 Enum 인지 검증하는 Annotation
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface EnumConstraint {
    String message() default "Enum에 존재하지 않는 값입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> value();
}
