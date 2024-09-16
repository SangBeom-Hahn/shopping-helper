package com.sketch2fashion.backend.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidImageExtensionValidator.class)
public @interface ValidImageExtension {

    String message() default "jpeg, jpg, png, heic 포멧 파일만 업로드 가능합니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
