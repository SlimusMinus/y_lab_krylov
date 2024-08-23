package org.example.service;

import org.example.dto.CarDTO;
import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.util.NotFoundException;
import org.example.util.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarStorage storage;

    private final ObjectValidator objectValidator;

    public CarService(CarStorage storage, ObjectValidator objectValidator) {
        this.storage = storage;
        this.objectValidator = objectValidator;
    }

    public List<Car> getAll() {
        return storage.getAll();
    }

    public List<CarDTO> getAllDTO(List<Car> cars) {
        return cars.stream()
                .map(CarMapper.INSTANCE::getCarDTO)
                .toList();
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

    public boolean isCarValidation(CarDTO carDTO, int id) {
        if (objectValidator.isValidObjectDTO(carDTO)) {
            Car car = CarMapper.INSTANCE.getCar(carDTO);
            if (id != 0) {
                car.setCar_id(id);
            }
            storage.saveOrUpdate(car);
            return true;
        } else {
            return false;
        }
    }

}
