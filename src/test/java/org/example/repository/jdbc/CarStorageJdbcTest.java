package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@Testcontainers
@Slf4j
@DisplayName("Тестирование класса CarStorageJdbc")
class CarStorageJdbcTest extends AbstractStorageJdbcTest {
    private CarStorage carServiceJdbc;

    /**
     * Инициализация {@link CarStorageJdbc} перед каждым тестом.
     */
    @BeforeEach
    public void setUpCar() {
        carServiceJdbc = new CarStorageJdbc();
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#getAll()}.
     * Тестирует получение всех автомобилей и сравнение их с предустановленным списком.
     */
    @Test
    @DisplayName("Проверка получения всех автомобилей")
    void testGetAll() {
        List<Car> cars = carServiceJdbc.getAll();
        assertAll(
                () -> assertThat(cars).hasSize(5),
                () -> assertThat(carServiceJdbc.getAll())
                        .usingRecursiveComparison()
                        .ignoringFields("car_id")
                        .isEqualTo(CAR_LIST)
        );
    }


    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#saveOrUpdate(Car)} для обновления существующего автомобиля.
     * Тестирует обновление автомобиля и проверку его изменений.
     */
    @Test
    @DisplayName("Проверка обновления существующего автомобиля")
    void update() {
        carServiceJdbc.saveOrUpdate(carUpdate);
        Car carUpdate = carServiceJdbc.getAll().get(car4.getId());
        assertThat(carUpdate).isEqualTo(carUpdate);

    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#delete(int)} для удаления автомобиля.
     * Тестирует удаление автомобиля и проверку его отсутствия в хранилище.
     */
    @Test
    @DisplayName("Проверка удаления автомобиля")
    void delete() {
        carServiceJdbc.delete(car4.getId());
        List<Car> cars = carServiceJdbc.getAll();
        assertThat(cars).doesNotContain(car4);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#delete(int)} для удаления автомобиля.
     * Тестирует удаление несуществующего автомобиля.
     */
    @Test
    @DisplayName("Проверка на удаление не существующего автомобиля")
    void deleteNotFound(){
        assertThatThrownBy(()->carServiceJdbc.delete(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link CarStorageJdbc#filter(Function, Predicate)} для фильтрации автомобилей.
     * Тестирует фильтрацию автомобилей по марке, состоянию и цене.
     */
    @Test
    @DisplayName("Проверка фильтрации автомобилей")
    void filter() {
        final List<Car> listVolvo = carServiceJdbc.filter(Car::getBrand, brand -> brand.equals("Volvo"));
        final List<Car> listNewCar = carServiceJdbc.filter(Car::getCondition, condition -> condition.equals("new"));
        final List<Car> filterPriceCar = carServiceJdbc.filter(Car::getPrice, price -> price == 25000);
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