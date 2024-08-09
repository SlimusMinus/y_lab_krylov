package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Car;
import org.example.repository.CarStorage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class CarStorageJdbc implements CarStorage {
    private Connection connection;

    public CarStorageJdbc() throws RuntimeException {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM car_shop.car";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("car_id"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setPrice(resultSet.getDouble("price"));
                car.setCondition(resultSet.getString("condition"));
                cars.add(car);
            }
        } catch (SQLException e) {
            log.error("Error fetching cars", e);
        }
        log.info("Get all cars {}", cars);
        return cars;
    }

    @Override
    public Car saveOrUpdate(Car car) {
        String query;
        if (car.getId() == 0) {
            query = "INSERT INTO car_shop.car (brand, model, year, price, condition) VALUES (?,?,?,?,?) RETURNING car_id";
        } else {
            query = "UPDATE car_shop.car SET brand=?, model=?, year=?, price=?, condition=? WHERE car_id=?";
        }
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            final Car newCar = getCar(car, statement);
            if (newCar != null){
                return newCar;
            }
        } catch (SQLException e) {
            log.error("Error saving or updating car", e);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM car_shop.car WHERE car_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            log.info("Car with id {} is deleting", id);
        } catch (SQLException e) {
            log.error("Error deleting car with id {}", id, e);
        }
    }

    @Override
    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        List<Car> cars = getAll();
        return cars.stream().filter(car -> predicate.test(getter.apply(car))).toList();
    }

    private static Car getCar(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getBrand());
        statement.setString(2, car.getModel());
        statement.setInt(3, car.getYear());
        statement.setDouble(4, car.getPrice());
        statement.setString(5, car.getCondition());
        if (car.getId() != 0) {
            statement.setInt(6, car.getId());
        }

        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            if (car.getId() == 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return car;
        }
        return null;
    }
}
