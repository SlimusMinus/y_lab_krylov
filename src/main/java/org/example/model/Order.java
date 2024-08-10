package org.example.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Представляет сущность заказа, содержащую информацию о пользователе, автомобиле, дате и статусе заказа.
 * Этот класс используется для хранения и управления данными о заказах в системе.
 * <p>
 * Класс включает поля:
 * <ul>
 *   <li><b>orderId</b> - уникальный идентификатор заказа.</li>
 *   <li><b>userId</b> - идентификатор пользователя, который создал заказ.</li>
 *   <li><b>carId</b> - идентификатор автомобиля, связанного с заказом.</li>
 *   <li><b>date</b> - дата создания заказа.</li>
 *   <li><b>status</b> - статус заказа (например, "заказ оформлен", "готов к выдаче").</li>
 * </ul>
 * </p>
 * <p>
 * Класс предоставляет два конструктора:
 * <ul>
 *   <li>Конструктор с параметрами для всех полей, включая идентификатор заказа.</li>
 *   <li>Конструктор без параметра идентификатора заказа, используемый при создании нового заказа.</li>
 * </ul>
 * </p>
 * <p>
 * Класс также включает аннотации от библиотеки Lombok:
 * <ul>
 *   <li>@Data - Генерирует методы getter, setter, toString, equals и hashCode.</li>
 *   <li>@AllArgsConstructor - Генерирует конструктор с параметрами для всех полей.</li>
 *   <li>@NoArgsConstructor - Генерирует конструктор без параметров.</li>
 * </ul>
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private int userId;
    private int carId;
    private LocalDate date;
    private String status;

    public Order(int userId, int carId, LocalDate date, String status) {
        this.userId = userId;
        this.carId = carId;
        this.date = date;
        this.status = status;
    }
}
