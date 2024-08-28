package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Объект передачи данных (DTO) для представления заказа.
 * <p>
 * Этот класс используется для передачи данных о заказе между различными слоями приложения.
 * Он включает информацию о пользователе, автомобиле, дате заказа и статусе заказа.
 * </p>
 *
 * <p>
 * Аннотации:
 * <ul>
 *     <li>{@code @Data} — автоматически генерирует геттеры, сеттеры, методы {@code toString()}, {@code equals()}, {@code hashCode()} и {@code canEqual()}.</li>
 *     <li>{@code @AllArgsConstructor} — генерирует конструктор со всеми полями класса в качестве параметров.</li>
 *     <li>{@code @NoArgsConstructor} — генерирует конструктор без параметров.</li>
 * </ul>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private static final int CURRENT_YEAR = 2024;
    @Positive(message = "userId должен быть положительным числом")
    private int userId;
    @Positive(message = "carId должен быть положительным числом")
    private int carId;
    @PastOrPresent(message = "Год должен быть не больше текущего года")
    private LocalDate date;
    @NotBlank(message = "поле статус не должно быть пустым")
    private String status;
}
