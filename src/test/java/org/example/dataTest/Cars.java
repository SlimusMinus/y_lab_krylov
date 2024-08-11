package org.example.dataTest;

import org.example.model.Car;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс {@code Cars} содержит набор предопределённых объектов {@link Car} и вспомогательные структуры данных для работы с ними.
 * <p>
 * Этот класс используется для инициализации и хранения данных о различных автомобилях, которые могут быть использованы в тестах или как пример данных.
 * </p>
 */
public class Cars {
    public static int CAR_id = 1;
    public static final int NOT_EXIST_ID = 800;
    public static final Car car1 = new Car(CAR_id++, "BMW", "M4", 2024, 25000, "new");
    public static final Car car2 = new Car(CAR_id++, "Volvo", "S40", 2023, 17500, "good");
    public static final Car car3 = new Car(CAR_id++, "Mercedes", "SLS", 2024, 25000, "new");
    public static final Car car4 = new Car(CAR_id++, "Volvo", "S60", 2019, 25000, "good");
    public static final Car car5 = new Car(CAR_id++, "Audi", "Q3", 2020, 19500.80, "good");
    public static final Car carSave = new Car("Toyota", "Camry", 2022, 15800, "good");
    public static final Car carUpdate = new Car(car4.getId(), "BMW", "S60", 2021, 25000, "good");
    public static final List<Car> CAR_LIST = List.of(car1, car2, car3, car4, car5);
    public static final List<Car> brandFilteredCars = List.of(car2, car4);
    public static final List<Car> conditionFilteredCars = List.of(car1, car3);
    public static final List<Car> priceFilteredCars = List.of(car1, car3, car4);

    public static Map<Integer, Car> cars = new HashMap<>();

    static {
        cars.put(car1.getId(), car1);
        cars.put(car2.getId(), car2);
        cars.put(car3.getId(), car3);
        cars.put(car4.getId(), car4);
        cars.put(car5.getId(), car5);
    }
}
