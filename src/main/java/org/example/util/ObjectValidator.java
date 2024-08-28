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

/**
 * Утилитный класс, предоставляющий методы для валидации DTO (Data Transfer Object)
 * с использованием API валидации Bean (JSR-380). Этот класс предназначен для проверки
 * соответствия DTO необходимым ограничениям валидации перед дальнейшей обработкой.
 *
 * <p>Класс использует {@link Validator} для выполнения валидации и поддерживает
 * обработку результатов валидации, возвращая соответствующий HTTP-статус и сообщение
 * об ошибке в случае неуспеха.
 * </p>
 *
 * <p>Пример использования:
 * <pre>
 *     ValidationDTO validationDTO = new ValidationDTO();
 *     boolean isValid = validationDTO.isValidObjectDTO(response, myObjectDTO);
 *     if (isValid) {
 *         // Объект прошел валидацию
 *     } else {
 *         // Объект не прошел валидацию
 *     }
 * </pre>
 * </p>
 *
 * <p>Валидация происходит в методе {@link #validateObject(Object)}. Если объект не
 * проходит валидацию, генерируется {@link IllegalArgumentException}, которое обрабатывается
 * методом {@link #isValidObjectDTO(HttpServletResponse, Object)}.
 * </p>
 *
 * <p>Основные функции класса:
 * <ul>
 *     <li>Создание валидатора с использованием {@link ValidatorFactory}.</li>
 *     <li>Валидация объекта DTO и генерация исключения при нарушении ограничений.</li>
 *     <li>Обработка исключений и формирование ответа с ошибкой валидации.</li>
 * </ul>
 * </p>
 *
 * @author Your Name
 */
@Slf4j
public class ObjectValidator {
    private final Validator validator;

    /**
     * Конструктор по умолчанию, создающий валидатор с использованием фабрики валидаторов.
     * Валидатор конфигурируется с использованием {@link ParameterMessageInterpolator}.
     */
    public ObjectValidator() {
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Валидация переданного объекта DTO. Если объект не соответствует ограничениям,
     * выбрасывается {@link IllegalArgumentException} с сообщением об ошибках валидации.
     *
     * @param <T> тип объекта DTO
     * @param objectDTO объект DTO для валидации
     * @throws IllegalArgumentException если объект не прошел валидацию
     */
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

    /**
     * Проверяет валидность переданного объекта DTO и, в случае неуспеха, отправляет
     * HTTP-ответ с кодом ошибки и сообщением. Возвращает true, если объект прошел
     * валидацию, и false, если нет.
     *
     * @param <T> тип объекта DTO
     * @param resp HTTP-ответ, в который будет записано сообщение об ошибке в случае неуспеха
     * @param objectDTO объект DTO для проверки
     * @return true, если объект прошел валидацию; false, если валидация провалена
     */
    public <T> boolean isValidObjectDTO(T objectDTO) {
        try {
            validateObject(objectDTO);
            return true;
        } catch (IllegalArgumentException exception) {
            log.error("Validation failed for OrderDTO: {}", objectDTO, exception);
            return false;
        }
    }
}
