package org.example.model;

import lombok.*;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String condition;

    public Car(String brand, String model, int year, double price, String condition) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }
}
