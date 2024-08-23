package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Car;
import org.example.util.NotFoundException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link CarStorage} с использованием JDBC для работы с базой данных.
 * <p>
 * Этот класс отвечает за выполнение операций CRUD (создание, чтение, обновление, удаление) с сущностью {@link Car}
 * в базе данных. Взаимодействие с базой данных осуществляется с помощью Spring JDBC и NamedParameterJdbcTemplate.
 * </p>
 */
@Slf4j
@Repository
public class CarStorage {

    private static final BeanPropertyRowMapper<Car> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Car.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insert;

    /**
     * Конструктор для инициализации зависимостей класса {@link CarStorage}.
     *
     * @param jdbcTemplate               объект для выполнения SQL-запросов и обновлений.
     * @param namedParameterJdbcTemplate объект для выполнения SQL-запросов с именованными параметрами.
     */
    public CarStorage(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("car")
                .usingGeneratedKeyColumns("car_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Возвращает список всех автомобилей, хранящихся в базе данных, отсортированный по идентификатору.
     *
     * @return список объектов {@link Car}.
     */
    public List<Car> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.car ORDER BY car_id", ROW_MAPPER);
    }

    /**
     * Возвращает автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля.
     * @return объект {@link Car}, если автомобиль найден, иначе null.
     */
    public Car getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM car_shop.car WHERE car_id=?", ROW_MAPPER, id);
    }

    /**
     * Сохраняет или обновляет информацию об автомобиле в базе данных.
     * <p>
     * Если идентификатор автомобиля равен 0, то выполняется вставка новой записи,
     * в противном случае выполняется обновление существующей записи.
     * </p>
     *
     * @param car объект {@link Car} для сохранения или обновления.
     * @return объект {@link Car} с обновленным идентификатором, если он был сгенерирован.
     */
    public Car saveOrUpdate(Car car) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("car_id", car.getCar_id())
                .addValue("brand", car.getBrand())
                .addValue("model", car.getModel())
                .addValue("year", car.getYear())
                .addValue("price", car.getPrice())
                .addValue("condition", car.getCondition());
        if (car.getCar_id() == 0) {
            Number newKey = insert.executeAndReturnKey(map);
            car.setCar_id(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("UPDATE car_shop.car set brand=:brand, model=:model, year=:year," +
                                                     "price=:price, condition=:condition WHERE car_id=:car_id", map) == 0) {
            return null;
        }
        return car;
    }

    /**
     * Удаляет автомобиль из базы данных по его идентификатору.
     *
     * @param id идентификатор автомобиля для удаления.
     * @throws NotFoundException если автомобиль с указанным идентификатором не найден.
     */
    public void delete(int id) {
        int count = jdbcTemplate.update("DELETE FROM car_shop.car WHERE car_id=?", id);
        if (count == 0) {
            throw new NotFoundException("Car with id " + id + " not found");
        }
    }


    /**
     * Фильтрует автомобили на основе заданного критерия.
     * <p>
     * Метод принимает функцию для получения значения из объекта автомобиля и предикат для проверки этого значения.
     * Возвращает список автомобилей, удовлетворяющих предикату.
     * </p>
     *
     * @param getter    функция для извлечения значения из объекта {@link Car}.
     * @param predicate предикат для фильтрации значений.
     * @param <T>       тип значения, возвращаемого функцией getter.
     * @return список объектов {@link Car}, удовлетворяющих условию предиката.
     */
    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        List<Car> cars = getAll();
        return cars.stream().filter(car -> predicate.test(getter.apply(car))).toList();
    }

}
