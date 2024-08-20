package org.example.service;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;
import org.example.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarStorage storage;

    public CarService(CarStorage storage) {
        this.storage = storage;
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

    public List<Car> getFilteredCars(String nameFilter, String params) {
        return switch (nameFilter) {
            case "brand" -> storage.filter(Car::getBrand, brand -> brand.equals(params));
            case "condition" -> storage.filter(Car::getCondition, condition -> condition.equals(params));
            case "price" -> storage.filter(Car::getPrice, price -> price == (Integer.parseInt(params)));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

}
