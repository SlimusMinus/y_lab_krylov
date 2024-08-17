package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

@Slf4j
public class ValidationDTO {
    private final Validator validator;

    public ValidationDTO() {
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public <T> void validateObject(T objectDTO) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public <T> boolean isValidObjectDTO(HttpServletResponse resp, T objectDTO) {
        resp.setCharacterEncoding("UTF-8");
        try {
            validateObject(objectDTO);
            return true;
        } catch (IllegalArgumentException exception) {
            log.error("Validation failed for OrderDTO: {}", objectDTO, exception);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().write(exception.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
    }

}
