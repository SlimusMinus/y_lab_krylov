package org.example.repository.inMemory.data;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Car;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, содержащий статические данные об автомобилях.
 *
 * <p>Этот класс предоставляет статические данные о различных автомобилях, включая их идентификаторы, бренды, модели, года выпуска,
 * цены и состояния. Он инициализирует коллекцию {@link Map} для хранения информации о каждом автомобиле, где ключом является уникальный
 * идентификатор автомобиля, а значением - объект {@link Car}.</p>
 *
 */
public class CarData {
    /**
     * Текущий идентификатор автомобиля, используемый для создания новых автомобилей.
     *
     * <p>Этот идентификатор автоматически увеличивается при создании нового автомобиля.</p>
     */
    @Getter
    private static int CarId = 1;
    private static final Car car1 = new Car(CarId++, "BMW", "M4", 2024, 25000, "new");
    private static final Car car2 = new Car(CarId++, "Volvo", "S40", 2023, 17500, "good");
    private static final Car car3 = new Car(CarId++, "Mercedes", "SLS", 2024, 25000, "new");
    private static final Car car4 = new Car(CarId++, "Volvo", "S60", 2019, 25000, "good");
    private static final Car car5 = new Car(CarId++, "Audi", "Q3", 2020, 19500.80, "good");

    /**
     * Коллекция для хранения информации о всех автомобилях.
     *
     * <p>Ключом является уникальный идентификатор автомобиля, а значением - объект {@link Car}.</p>
     */
    @Getter
    @Setter
    private static Map<Integer, Car> cars = new HashMap<>();

    static {
        cars.put(car1.getId(), car1);
        cars.put(car2.getId(), car2);
        cars.put(car3.getId(), car3);
        cars.put(car4.getId(), car4);
        cars.put(car5.getId(), car5);
    }
}
