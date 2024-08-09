package org.example.repository.inMemory;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.inMemory.data.CarData;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Cars.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Тестирования сервиса автомобилей на ")
class CarsStorageInMemoryTest {
    private CarStorage carStorage;
    @BeforeEach
    void setUp() {
        carStorage = new CarStorageInMemory();
    }

    @AfterEach
    void updateAllList(){
        CarData.setCars(cars);
    }

    @DisplayName("получение полного списка автомобилей")
    @Test
    void getAll() {
        assertThat(carStorage.getAll()).isNotNull();
        assertThat(carStorage.getAll()).containsAll(CAR_LIST);
    }

    @DisplayName("сохранение нового автомобиля")
    @Test
    void save() {
        carStorage.saveOrUpdate(carSave1);
        Car newCar = carStorage.getAll().get(carStorage.getAll().size() - 1);
        assertThat(newCar).isEqualTo(carSave1);
    }

    @DisplayName("обновление данных об имеющемся автомобиле")
    @Test
    void update() {
        carStorage.saveOrUpdate(carUpdate);
        Car carUpdate = carStorage.getAll().get(car4.getId());
        assertThat(carUpdate).isEqualTo(carUpdate);
    }

    @Test
    @DisplayName("удаление автомобиля и списка")
    void delete() {
        carStorage.delete(car1.getId());
        assertThat(CarData.getCars()).doesNotContainKey(car1.getId());
    }

    @DisplayName(" добавление фильтрации по брэнду, состоянию и цене")
    @Test
    void filter() {
        final List<Car> listVolvo = carStorage.filter(Car::getBrand, brand -> brand.equals("Volvo"));
        final List<Car> listNewCar = carStorage.filter(Car::getCondition, condition -> condition.equals("new"));
        final List<Car> filterPriceCar = carStorage.filter(Car::getPrice, price -> price == 25000);
        assertAll(
                () -> assertThat(listVolvo).containsAll(brandFilteredCars),
                () -> assertThat(listNewCar).containsAll(conditionFilteredCars),
                () -> assertThat(filterPriceCar).containsAll(priceFilteredCars)
        );
    }
}