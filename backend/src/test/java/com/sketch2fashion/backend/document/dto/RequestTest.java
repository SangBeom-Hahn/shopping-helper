package com.sketch2fashion.backend.document.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;

import static com.sketch2fashion.backend.controller.dto.ValidateMessage.EMPTY_MESSAGE;

public class RequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected Set<ConstraintViolation<Object>> getConstraintViolation(Object object) {
        return validator.validate(object);
    }

    protected boolean isEmpty(Object object) {
        return getConstraintViolation(object).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE));
    }
}
