package com.sketch2fashion.backend.utils.validator;

import com.sketch2fashion.backend.domain.file.FileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidImageExtensionValidator implements ConstraintValidator<ValidImageExtension, MultipartFile> {

    private final List<String> validExtensions = List.of(".jpeg", ".JPEG", ".jpg", ".JPG", ".png", ".PNG", ".heic", ".HEIC");

    @Override
    public boolean isValid(final MultipartFile value, final ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        return validExtensions.contains(FileExtension.getExtension(value.getOriginalFilename()));
    }
}
