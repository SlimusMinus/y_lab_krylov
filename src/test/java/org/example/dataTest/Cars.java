package org.example.dataTest;

import org.example.dto.CarDTO;
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
    public static int CAR_ID = 1;
    public static final int NOT_EXIST_ID = 800;
    public static final int GET_CAR_ID = 2;
    public static final Car car1 = new Car(CAR_ID++, "BMW", "M4", 2024, 25000, "new");
    public static final Car car2 = new Car(CAR_ID++, "Volvo", "S40", 2023, 17500, "good");
    public static final Car car3 = new Car(CAR_ID++, "Mercedes", "SLS", 2024, 25000, "new");
    public static final Car car4 = new Car(CAR_ID++, "Volvo", "S60", 2019, 25000, "good");
    public static final Car car5 = new Car(CAR_ID++, "Audi", "Q3", 2020, 19500.80, "good");
    public static final Car carSave = new Car("Toyota", "Camry", 2022, 15800, "good");
    public static final Car carUpdate = new Car(car4.getCar_id(), "BMW", "S60", 2021, 25000, "good");
    public static final CarDTO updatedCarDTO = new CarDTO("BMW", "M4", 2024, 27000.0, "new");
    public static final CarDTO newCarDTO = new CarDTO("Tesla", "Model S", 2022, 79999.99, "new");
    public static final List<Car> CAR_LIST = List.of(car1, car2, car3, car4, car5);
    public static final List<Car> brandFilteredCars = List.of(car2, car4);
    public static final List<Car> conditionFilteredCars = List.of(car1, car3);
    public static final List<Car> priceFilteredCars = List.of(car1, car3, car4);

    public static Map<Integer, Car> cars = new HashMap<>();

    static {
        cars.put(car1.getCar_id(), car1);
        cars.put(car2.getCar_id(), car2);
        cars.put(car3.getCar_id(), car3);
        cars.put(car4.getCar_id(), car4);
        cars.put(car5.getCar_id(), car5);
    }
}