package org.example.service;

import org.example.model.Car;
import org.example.repository.CarData;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.DataTest.Cars.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Тестирования сервиса автомобилей на ")
class CarsServiceImplTest {
    private CarService carService;
    @BeforeEach
    void setUp() {
        carService = new CarServiceImpl();
    }

    @AfterEach
    void updateAllList(){
        CarData.setCars(cars);
    }

    @DisplayName("получение полного списка автомобилей")
    @Test
    void getAll() {
        assertThat(carService.getAll()).isNotNull();
        assertThat(carService.getAll()).containsAll(listCars);
    }

    @DisplayName("сохранение нового автомобиля")
    @Test
    void save() {
        carService.saveOrUpdate(carSave);
        Car newCar = carService.getAll().get(carService.getAll().size() - 1);
        assertThat(newCar).isEqualTo(carSave);
    }

    @DisplayName("обновление данных об имеющемся автомобиле")
    @Test
    void update() {
        carService.saveOrUpdate(carUpdate);
        Car carUpdate = carService.getAll().get(car4.getId());
        assertThat(carUpdate).isEqualTo(carUpdate);
    }

    @Test
    @DisplayName("удаление автомобиля и списка")
    void delete() {
        carService.delete(car1.getId());
        assertThat(CarData.getCars()).doesNotContainKey(car1.getId());
    }

    @DisplayName(" добавление фильтрации по брэнду, состоянию и цене")
    @Test
    void filter() {
        final List<Car> listVolvo = carService.filter(Car::getBrand, brand -> brand.equals("Volvo"));
        final List<Car> listNewCar = carService.filter(Car::getCondition, condition -> condition.equals("new"));
        final List<Car> filterPriceCar = carService.filter(Car::getPrice, price -> price == 25000);
        assertAll(
                () -> assertThat(listVolvo).containsAll(brandFilteredCars),
                () -> assertThat(listNewCar).containsAll(conditionFilteredCars),
                () -> assertThat(filterPriceCar).containsAll(priceFilteredCars)
        );
    }
}