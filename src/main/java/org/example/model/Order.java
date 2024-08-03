package org.example.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Класс представляет заказ, включающий информацию о дате, автомобиле, пользователе и статусе заказа.
 *
 * <p>Класс использует аннотации Lombok для автоматического создания геттеров, сеттеров, методов {@code toString}, {@code equals} и {@code hashCode}, а также для создания конструктора с параметрами и конструктора без параметров.</p>
 *
 * <p>Аннотации:</p>
 * <ul>
 *   <li>{@code @Getter} - автоматически создает геттеры для всех полей.</li>
 *   <li>{@code @Setter} - автоматически создает сеттеры для всех полей.</li>
 *   <li>{@code @EqualsAndHashCode} - автоматически создает методы {@code equals} и {@code hashCode}.</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор с параметрами для всех полей.</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор без параметров.</li>
 *   <li>{@code @ToString} - автоматически создает метод {@code toString}.</li>
 * </ul>
 *
 * <p>Пример использования:</p>
 * <pre>
 *     Order order = new Order(1, LocalDate.now(), new Car(), new User(), "In progress");
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private int OrderId;
    private LocalDate date;
    private Car car;
    private User user;
    private String status;

    public Order(int orderId, LocalDate date, Car car, String status) {
        OrderId = orderId;
        this.date = date;
        this.car = car;
        this.status = status;
    }
}
