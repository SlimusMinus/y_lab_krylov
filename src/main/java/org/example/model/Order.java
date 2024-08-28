package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
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
@Table(name = "orders", schema = "car_shop")
public class Order {
    @Id
    @SequenceGenerator(schema = "car_shop", name = "orders_order_id_seq", sequenceName = "orders_order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_order_id_seq")
    private int orderId;
    @Column(name = "userId")
    @Positive(message = "userId должен быть положительным числом")
    private int userId;
    @Column(name = "carId")
    @Positive(message = "carId должен быть положительным числом")
    private int carId;
    @Column(name = "date")
    @PastOrPresent(message = "Год должен быть не больше текущего года")
    private LocalDate date;
    @Column(name = "status")
    @NotBlank(message = "поле статус не должно быть пустым")
    private String status;
    private static final int CURRENT_YEAR = 2024;

    public Order(int userId, int carId, LocalDate date, String status) {
        this.userId = userId;
        this.carId = carId;
        this.date = date;
        this.status = status;
    }
}
