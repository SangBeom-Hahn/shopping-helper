package com.sketch2fashion.backend.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = ValidImageExistsValidator.class)
public @interface ValidImageExists {

    String message() default "빈 파일은 입력할 수 없습니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
