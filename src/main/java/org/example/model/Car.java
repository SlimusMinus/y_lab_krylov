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
 *   <li>{@code @Getter} - автоматически создает геттеры для всех полей.</li>
 *   <li>{@code @Setter} - автоматически создает сеттеры для всех полей.</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор с параметрами для всех полей.</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор без параметров.</li>
 *   <li>{@code @ToString} - автоматически создает метод {@code toString}.</li>
 *   <li>{@code @EqualsAndHashCode} - автоматически создает методы {@code equals} и {@code hashCode}.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Car {
    int id;
    String brand;
    String model;
    int year;
    double price;
    private String condition;
}
