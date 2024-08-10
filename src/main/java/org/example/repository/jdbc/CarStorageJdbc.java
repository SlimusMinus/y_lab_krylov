package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.util.NotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link CarStorage} с использованием JDBC для взаимодействия с базой данных.
 * <p>
 * Этот класс обеспечивает основные операции CRUD (создание, чтение, обновление, удаление) для объектов {@link Car},
 * а также позволяет фильтровать список автомобилей по различным критериям.
 * </p>
 */
@Slf4j
public class CarStorageJdbc implements CarStorage {
    private Connection connection;

    /**
     * Конструктор класса, который устанавливает соединение с базой данных.
     * <p>
     * В случае ошибки при подключении, выбрасывается исключение {@link RuntimeException}.
     * </p>
     */
    public CarStorageJdbc() throws RuntimeException {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

    /**
     * Возвращает список всех автомобилей, хранящихся в базе данных.
     *
     * @return Список объектов {@link Car}.
     */
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

    /**
     * Сохраняет или обновляет запись об автомобиле в базе данных.
     * <p>
     * Если идентификатор автомобиля равен 0, будет выполнена вставка новой записи.
     * В противном случае запись обновляется.
     * </p>
     *
     * @param car Объект {@link Car}, который нужно сохранить или обновить.
     * @return Сохраненный или обновленный объект {@link Car}, либо {@code null} в случае ошибки.
     */
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

    /**
     * Удаляет автомобиль из базы данных по его идентификатору.
     *
     * @param id Идентификатор автомобиля, который нужно удалить.
     */
    @Override
    public void delete(int id) {
        if(id > getAll().size()){
            log.error("Not found car with id {}", id);
            throw new NotFoundException("Id такого автомобиля не существует");
        }
        else{
            String query = "DELETE FROM car_shop.car WHERE car_id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
                log.info("Car with id {} is deleting", id);
            } catch (SQLException e) {
                log.error("Error deleting car with id {}", id, e);
            }
        }
    }

    /**
     * Фильтрует список автомобилей на основе переданного критерия.
     * <p>
     * Возвращает список автомобилей, удовлетворяющих заданному условию.
     * </p>
     *
     * @param getter    Функция для получения поля объекта {@link Car}.
     * @param predicate Условие, которое должно быть выполнено для включения автомобиля в результат.
     * @param <T>       Тип возвращаемого значения функции getter.
     * @return Список объектов {@link Car}, которые соответствуют условию.
     */
    @Override
    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        List<Car> cars = getAll();
        return cars.stream().filter(car -> predicate.test(getter.apply(car))).toList();
    }

    /**
     * Вспомогательный метод для установки параметров и выполнения SQL-запроса для сохранения или обновления автомобиля.
     *
     * @param car       Объект {@link Car}, который нужно сохранить или обновить.
     * @param statement Подготовленный SQL-запрос.
     * @return Сохраненный или обновленный объект {@link Car}, либо {@code null}, если операция не удалась.
     * @throws SQLException Если произошла ошибка при выполнении SQL-запроса.
     */
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
