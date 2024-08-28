package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * Класс представляет автомобиль и его основные характеристики.
 * Содержит информацию о марке, модели, годе выпуска, цене и состоянии автомобиля.
 *
 * <p>Класс использует аннотации Lombok для автоматического создания геттеров, сеттеров, методов {@code toString}, {@code equals} и {@code hashCode}, а также для создания конструктора с параметрами, конструктора без параметров и построителя.</p>
 *
 * <p>Аннотации:</p>
 * <ul>
 *   <li>@Data - Генерирует методы getter, setter, toString, equals и hashCode.</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор с параметрами для всех полей.</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор без параметров.</li>
 * </ul>
 */

@NamedQueries({
    @NamedQuery(name = Car.ALL_SORTED, query = "SELECT c FROM Car c ORDER BY c.car_id")
})
@Entity
@Table(schema = "car_shop", name = "car")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    public static final String DELETE = "User.delete";
    public static final String BY_EMAIL = "User.getByEmail";
    public static final String ALL_SORTED = "User.getAllSorted";

    @Id
    @SequenceGenerator(schema = "car_shop", name = "car_car_id_seq", sequenceName = "car_car_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_car_id_seq")
    private int car_id;

    @Column(name = "brand")
    @NotBlank(message = "поле бренд не должно быть пустым")
    private String brand;

    @Column(name = "model")
    @NotBlank(message = "поле модель не должно быть пустым")
    private String model;

    @Column(name = "year")
    @Max(value = CURRENT_YEAR, message = "год выпуска не должен быть больше текущего года")
    private int year;

    @Column(name = "price")
    @Positive(message = "Цена должна быть положительным числом")
    private double price;

    @Column(name = "condition")
    @NotBlank(message = "поле состояние не должно быть пустым")
    private String condition;

    private static final int CURRENT_YEAR = 2024;

    public Car(String brand, String model, int year, double price, String condition) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }
}
