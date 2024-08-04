package org.example.DataTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cars {

    public static int CarId = 1;
    public static final org.example.model.Car car1 = new org.example.model.Car(CarId++, "BMW", "M4", 2024, 25000, "new");
    public static final org.example.model.Car car2 = new org.example.model.Car(CarId++, "Volvo", "S40", 2023, 17500, "good");
    public static final org.example.model.Car car3 = new org.example.model.Car(CarId++, "Mercedes", "SLS", 2024, 25000, "new");
    public static final org.example.model.Car car4 = new org.example.model.Car(CarId++, "Volvo", "S60", 2019, 25000, "good");
    public static final org.example.model.Car car5 = new org.example.model.Car(CarId++, "Audi", "Q3", 2020, 19500.80, "good");
    public static final org.example.model.Car carSave = new org.example.model.Car(0, "Audi", "Q7", 2024, 20098, "new");
    public static final org.example.model.Car carUpdate = new org.example.model.Car(car4.getId(), "BMW", "S60", 2021, 25000, "good");

    public static final List<org.example.model.Car> listCars = List.of(car1, car2, car3, car4, car5);
    public static final List<org.example.model.Car> brandFilteredCars = List.of(car2, car4);
    public static final List<org.example.model.Car> conditionFilteredCars = List.of(car1, car3);
    public static final List<org.example.model.Car> priceFilteredCars = List.of(car1, car3, car4);

    public static Map<Integer, org.example.model.Car> cars = new HashMap<>();

    static {
        cars.put(car1.getId(), car1);
        cars.put(car2.getId(), car2);
        cars.put(car3.getId(), car3);
        cars.put(car4.getId(), car4);
        cars.put(car5.getId(), car5);
    }

}
