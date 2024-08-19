package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * Data Transfer Object (DTO) для представления информации о автомобиле.
 * Этот класс используется для передачи данных о транспортных средствах между различными слоями приложения.
 * <p>
 * Класс содержит информацию о бренде, модели, годе выпуска, цене и состоянии автомобиля.
 * Поля класса аннотированы для валидации входных данных.
 * </p>
 *
 * <p>
 * Поля:
 * <ul>
 *     <li>{@link #brand} - бренд автомобиля. Поле не должно быть пустым.</li>
 *     <li>{@link #model} - модель автомобиля. Поле не должно быть пустым.</li>
 *     <li>{@link #year} - год выпуска автомобиля. Поле не должно превышать текущий год.</li>
 *     <li>{@link #price} - цена автомобиля. Поле должно быть положительным числом.</li>
 *     <li>{@link #condition} - состояние автомобиля. Поле не должно быть пустым.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Пример использования:
 * <pre>
 *     CarDTO carDTO = new CarDTO("Toyota", "Camry", 2020, 25000, "New");
 * </pre>
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private static final int CURRENT_YEAR = 2024;
    @NotBlank(message = "поле бренд не должно быть пустым")
    private String brand;
    @NotBlank(message = "поле модель не должно быть пустым")
    private String model;
    @Max(value = CURRENT_YEAR, message = "год выпуска не должен быть больше текущего года")
    private int year;
    @Positive(message = "Цена должна быть положительным числом")
    private double price;
    @NotBlank(message = "поле состояние не должно быть пустым")
    private String condition;
}
