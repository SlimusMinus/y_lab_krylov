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
public class CarStorageJdbc implements CarStorage, AutoCloseable {
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
     * Закрывает соединение с базой данных.
     * <p>
     * Если соединение с базой данных было установлено (не равно {@code null}),
     * метод пытается его закрыть. В случае успешного закрытия в журнал записывается
     * информационное сообщение. Если возникает ошибка при закрытии соединения,
     * она записывается в журнал как ошибка.
     * </p>
     *
     * @throws SQLException Если произошла ошибка при закрытии соединения.
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                log.info("Connection closed successfully.");
            } catch (SQLException e) {
                log.error("Error closing connection", e);
            }
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
        String query = "SELECT * FROM car_shop.car ORDER BY car_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Car car = new Car();
                setParamsCar(car, resultSet);
                cars.add(car);
            }
        } catch (SQLException e) {
            log.error("Error fetching cars", e);
        }
        log.info("Get all cars {}", cars);
        return cars;
    }

    /**
     * Возвращает объект {@link Car} по его идентификатору из базы данных.
     * <p>
     * Метод выполняет SQL-запрос для поиска автомобиля по заданному идентификатору.
     * Если автомобиль найден, он возвращается в виде объекта {@link Car}.
     * Если автомобиль с данным идентификатором не найден, метод возвращает {@code null}.
     * </p>
     *
     * @param id Идентификатор автомобиля, который нужно найти.
     * @return Объект {@link Car}, соответствующий заданному идентификатору,
     * или {@code null}, если автомобиль не найден.
     */
    @Override
    public Car getById(int id) {
        Car car = null;
        String query = "SELECT * FROM car_shop.car WHERE car_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = new Car();
                    setParamsCar(car, resultSet);
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching car with id {}", id, e);
        }
        return car;
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
            final Car newCar = getEditCar(car, statement);
            if (newCar != null) {
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
        boolean carNotExists = getAll().stream().noneMatch(car -> car.getId() == id);
        if (carNotExists) {
            log.error("Not found car with id {}", id);
            throw new NotFoundException("Id такого автомобиля не существует");
        }
        String query = "DELETE FROM car_shop.car WHERE car_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            log.info("Car with id {} is deleting", id);
        } catch (SQLException e) {
            log.error("Error deleting car with id {}", id, e);
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
    private static Car getEditCar(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getBrand());
        statement.setString(2, car.getModel());
        statement.setInt(3, car.getYear());
        statement.setDouble(4, car.getPrice());
        statement.setString(5, car.getCondition());
        if (car.getId() != 0) {
            statement.setInt(6, car.getId());
        }
        int affectedRows = statement.executeUpdate();
        return processGeneratedKeys(car, statement, affectedRows);
    }

    /**
     * Обрабатывает сгенерированные ключи после выполнения SQL-запроса на вставку или обновление данных.
     * <p>
     * Если был вставлен новый объект, метод извлекает сгенерированный идентификатор (ключ)
     * и устанавливает его в объект {@link Car}. Если был обновлен существующий объект,
     * метод возвращает его без изменений.
     * </p>
     *
     * @param car Объект {@link Car}, который был сохранен или обновлен.
     * @param statement Подготовленный SQL-запрос, выполненный для вставки или обновления данных.
     * @param affectedRows Количество затронутых строк в результате выполнения SQL-запроса.
     * @return Объект {@link Car} с установленным идентификатором, если он был сгенерирован,
     * или {@code null}, если не было затронуто ни одной строки.
     * @throws SQLException Если произошла ошибка при извлечении сгенерированных ключей.
     */
    private static Car processGeneratedKeys(Car car, PreparedStatement statement, int affectedRows) throws SQLException {
        if (affectedRows == 0) {
            return null;
        }
        if (car.getId() != 0) {
            return car;
        }
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                car.setId(generatedKeys.getInt(1));
            }
        }
        return car;
    }

    /**
     * Устанавливает параметры объекта {@link Car} на основе данных, извлеченных из объекта {@link ResultSet}.
     * <p>
     * Метод извлекает данные из текущей строки результирующего набора и заполняет
     * объект {@link Car} соответствующими значениями полей.
     * </p>
     *
     * @param car Объект {@link Car}, который нужно заполнить данными.
     * @param resultSet Объект {@link ResultSet}, содержащий данные из базы данных.
     * @throws SQLException Если произошла ошибка при извлечении данных из {@link ResultSet}.
     */
    private static void setParamsCar(Car car, ResultSet resultSet) throws SQLException {
        car.setId(resultSet.getInt("car_id"));
        car.setBrand(resultSet.getString("brand"));
        car.setModel(resultSet.getString("model"));
        car.setYear(resultSet.getInt("year"));
        car.setPrice(resultSet.getDouble("price"));
        car.setCondition(resultSet.getString("condition"));
    }
}
