package org.example.service;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;
import org.example.util.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CarService {
    private final CarStorage storage;

    public CarService() {
        storage = new CarStorageJdbc();
    }

    public List<Car> getAll() {
        return storage.getAll();
    }

    public Car getById(int id) {
        return storage.getById(id);
    }

    public Car saveOrUpdate(Car car) {
        return storage.saveOrUpdate(car);
    }

    public void delete(int id) {
        storage.delete(id);
    }

    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        return storage.filter(getter, predicate);
    }

    public List<Car> getFilteredCars(String nameFilter, String params) {
        return switch (nameFilter) {
            case "brand" -> filter(Car::getBrand, brand -> brand.equals(params));
            case "condition" -> filter(Car::getCondition, condition -> condition.equals(params));
            case "price" -> filter(Car::getPrice, price -> price == (Integer.parseInt(params)));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

}
