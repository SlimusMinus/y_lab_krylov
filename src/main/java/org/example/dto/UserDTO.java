package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Order;
import org.example.model.Roles;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * Объект передачи данных (DTO) для представления пользователя.
 * <p>
 * Этот класс используется для передачи данных о пользователе между различными слоями приложения.
 * Включает информацию о пользователе, его ролях и заказах.
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
public class UserDTO {
    @NotBlank(message = "поле имя не должно быть пустым")
    private String name;
    @Min(value = 18, message = "минимальный возраст 18 лет")
    @Max(value = 120, message = "максимальный возраст 120 лет")
    private int age;
    @NotBlank(message = "поле город не должно быть пустым")
    private String city;
    private Set<Roles> role;
    private List<Order> orders;
}
