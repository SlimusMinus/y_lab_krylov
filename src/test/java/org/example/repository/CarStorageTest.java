package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.AppConfigTest;
import org.example.model.Car;
import org.example.service.CarService;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Cars.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link CarStorage}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Testcontainers
@Slf4j
@SpringJUnitConfig(AppConfigTest.class)
@DisplayName("Тестирование класса CarStorageJdbc")
public class CarStorageTest extends AbstractStorageTest {

    @Autowired
    @Qualifier("carServiceTest")
    private CarService service;

    /**
     * Проверяет корректность работы метода {@link CarStorage#getAll()}.
     * Тестирует получение всех автомобилей и сравнение их с предустановленным списком.
     */
    @Test
    @DisplayName("Проверка получения всех автомобилей")
    void testGetAll() {
        List<Car> cars = service.getAll();
        assertAll(
                () -> assertThat(cars).hasSize(5),
                () -> assertThat(service.getAll())
                        .usingRecursiveComparison()
                        .ignoringFields("car_id")
                        .isEqualTo(CAR_LIST)
        );
    }

    /**
     * Тестирует метод {@link CarStorage#getById(int)}.
     * <p>
     * Проверяет, что метод возвращает автомобиль с заданным идентификатором,
     * и что полученный объект совпадает с ожидаемым значением.
     * </p>
     */
    @Test
    @DisplayName("Тестирование метода getById(int) для получения автомобиля по идентификатору")
    void getById() {
        Car car = service.getById(GET_CAR_ID);
        assertThat(car2).isEqualTo(car);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorage#saveOrUpdate(Car)} для обновления существующего автомобиля.
     * Тестирует обновление автомобиля и проверку его изменений.
     */
    @Test
    @DisplayName("Проверка обновления существующего автомобиля")
    void update() {
        service.saveOrUpdate(carUpdate);
        Car carUpdate = service.getAll().get(car4.getCar_id());
        assertThat(carUpdate).isEqualTo(carUpdate);

    }

    /**
     * Проверяет корректность работы метода {@link CarStorage#delete(int)} для удаления автомобиля.
     * Тестирует удаление автомобиля и проверку его отсутствия в хранилище.
     */
    @Test
    @DisplayName("Проверка удаления автомобиля")
    void delete() {
        service.delete(car4.getCar_id());
        List<Car> cars = service.getAll();
        assertThat(cars).doesNotContain(car4);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorage#delete(int)} для удаления автомобиля.
     * Тестирует удаление несуществующего автомобиля.
     */
    @Test
    @DisplayName("Проверка на удаление не существующего автомобиля")
    void deleteNotFound() {
        assertThatThrownBy(() -> service.delete(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorage#filter(Function, Predicate)} для фильтрации автомобилей.
     * Тестирует фильтрацию автомобилей по марке, состоянию и цене.
     */
    @Test
    @DisplayName("Проверка фильтрации автомобилей")
    void filter() {
        final List<Car> listVolvo = service.getFilteredCars("brand", "Volvo");
        final List<Car> listNewCar = service.getFilteredCars("condition", "new");
        final List<Car> filterPriceCar = service.getFilteredCars("price", "25000");
        assertAll(
                () -> assertThat(listVolvo).containsAll(brandFilteredCars),
                () -> assertThat(listNewCar).containsAll(conditionFilteredCars),
                () -> assertThat(filterPriceCar).containsAll(priceFilteredCars)
        );
    }

    /**
     * Скрипт для создания таблицы автомобилей в testContainer
     */
    @Override
    protected String createTable() {
        return """
                DROP TABLE IF EXISTS car_shop.car;
                CREATE TABLE car_shop.car (
                    car_id SERIAL PRIMARY KEY,
                    brand TEXT,
                    model TEXT,
                    year INTEGER,
                    price NUMERIC,
                    condition TEXT
                );
                """;
    }

    /**
     * Скрипт для заполнения таблицы автомобилей в testContainer
     */
    @Override
    protected String populateTable() {
        return """
                INSERT INTO car_shop.car (brand, model, year, price, condition) VALUES
                              ('BMW', 'M4', 2024, 25000.00, 'new'),
                              ('Volvo', 'S40', 2023, 17500.00, 'good'),
                              ('Mercedes', 'SLS', 2024, 25000.00, 'new'),
                              ('Volvo', 'S60', 2019, 25000.00, 'good'),
                              ('Audi', 'Q3', 2020, 19500.80, 'good');
                """;
    }
}
