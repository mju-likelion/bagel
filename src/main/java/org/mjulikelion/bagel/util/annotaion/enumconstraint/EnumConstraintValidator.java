package org.mjulikelion.bagel.util.annotaion.enumconstraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumConstraintValidator implements ConstraintValidator<EnumConstraint, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumConstraint constraintAnnotation) {
        enumClass = constraintAnnotation.value();
    }

    /**
     * 주어진 값이 Enum에 존재하는지 검증
     *
     * @param value   검증 대상이 되는 Enum 값
     * @param context ConstraintValidatorContext 객체
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
