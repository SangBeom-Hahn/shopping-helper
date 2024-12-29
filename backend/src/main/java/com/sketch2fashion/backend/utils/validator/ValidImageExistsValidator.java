package com.sketch2fashion.backend.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidImageExistsValidator implements ConstraintValidator<ValidImageExists, MultipartFile> {

    @Override
    public boolean isValid(final MultipartFile value, final ConstraintValidatorContext context) {
        return value != null && !value.isEmpty();
    }
}
