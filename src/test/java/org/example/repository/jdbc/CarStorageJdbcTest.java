package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Car;
import org.example.service.CarService;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Cars.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link CarStorageJdbc}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Slf4j
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring/spring-test-config.xml", "classpath:spring/spring-db-test.xml"})
@DisplayName("Тестирование класса CarStorageJdbc")
class CarStorageJdbcTest extends AbstractStorageJdbcTest {

    @Autowired
    private CarService storage;

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#getAll()}.
     * Тестирует получение всех автомобилей и сравнение их с предустановленным списком.
     */
    @Test
    @DisplayName("Проверка получения всех автомобилей")
    void testGetAll() {
        List<Car> cars = storage.getAll();
        assertAll(
                () -> assertThat(cars).hasSize(5),
                () -> assertThat(storage.getAll())
                        .usingRecursiveComparison()
                        .isEqualTo(CAR_LIST)
        );
    }

    /**
     * Тестирует метод {@link CarStorageJdbc#getById(int)}.
     * <p>
     * Проверяет, что метод возвращает автомобиль с заданным идентификатором,
     * и что полученный объект совпадает с ожидаемым значением.
     * </p>
     */
    @Test
    @DisplayName("Тестирование метода getById(int) для получения автомобиля по идентификатору")
    void getById() {
        Car car = storage.getById(GET_CAR_ID);
        assertThat(car2).isEqualTo(car);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#saveOrUpdate(Car)} для обновления существующего автомобиля.
     * Тестирует обновление автомобиля и проверку его изменений.
     */
    @Test
    @DisplayName("Проверка обновления существующего автомобиля")
    void update() {
        storage.saveOrUpdate(carUpdate);
        Car carUpdate = storage.getAll().get(car4.getCar_id());
        assertThat(carUpdate).isEqualTo(carUpdate);

    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#delete(int)} для удаления автомобиля.
     * Тестирует удаление автомобиля и проверку его отсутствия в хранилище.
     */
    @Test
    @DisplayName("Проверка удаления автомобиля")
    void delete() {
        storage.delete(car4.getCar_id());
        List<Car> cars = storage.getAll();
        assertThat(cars).doesNotContain(car4);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#delete(int)} для удаления автомобиля.
     * Тестирует удаление несуществующего автомобиля.
     */
    @Test
    @DisplayName("Проверка на удаление не существующего автомобиля")
    void deleteNotFound(){
        assertThatThrownBy(()-> storage.delete(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#filter(Function, Predicate)} для фильтрации автомобилей.
     * Тестирует фильтрацию автомобилей по марке, состоянию и цене.
     */
    @Test
    @DisplayName("Проверка фильтрации автомобилей")
    void filter() {
        final List<Car> listVolvo = storage.getFilteredCars("brand", "Volvo");
        final List<Car> listNewCar = storage.getFilteredCars("condition", "new");
        final List<Car> filterPriceCar = storage.getFilteredCars("price", "25000");
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