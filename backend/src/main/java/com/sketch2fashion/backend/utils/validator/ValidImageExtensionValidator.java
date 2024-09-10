package com.sketch2fashion.backend.utils.validator;

import com.sketch2fashion.backend.domain.file.FileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

public class ValidImageExtensionValidator implements ConstraintValidator<ValidImageExtension, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        return FileExtension.isValidFormat(value.getOriginalFilename());
    }
}
