package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Cars.*;
import static org.junit.jupiter.api.Assertions.assertAll;


@Testcontainers
@Slf4j
class CarStorageJdbcTest extends AbstractStorageJdbcTest {
    private CarStorage carServiceJdbc;

    @BeforeEach
    public void setUpCar(){
        carServiceJdbc = new CarStorageJdbc();
    }

    @Test
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

    @Test
    void save() {
        carServiceJdbc.saveOrUpdate(carSave1);
        Car newCar = carServiceJdbc.getAll().get(carServiceJdbc.getAll().size() - 1);
        assertThat(carSave1).isEqualTo(newCar);
    }

    @Test
    void update() {
        carServiceJdbc.saveOrUpdate(carUpdate);
        Car carUpdate = carServiceJdbc.getAll().get(car4.getId());
        assertThat(carUpdate).isEqualTo(carUpdate);

    }

    @Test
    void delete() {
        carServiceJdbc.delete(car4.getId());
        List<Car> cars = carServiceJdbc.getAll();
        assertThat(cars).doesNotContain(car4);
    }

    @Test
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

    @Override
    String createTable() {
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

    @Override
    String populateTable() {
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