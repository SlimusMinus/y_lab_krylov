package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.util.NotFoundException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Repository
public class CarStorageJdbc implements CarStorage {

    private static final BeanPropertyRowMapper<Car> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Car.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insert;

    public CarStorageJdbc(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("car")
                .usingGeneratedKeyColumns("car_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Car> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.car ORDER BY car_id", ROW_MAPPER);
    }

    @Override
    public Car getById(int id) {
        final List<Car> carList = jdbcTemplate.query("SELECT * FROM car_shop.car WHERE car_id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(carList);
    }

    @Override
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

    @Override
    public void delete(int id) {
        int count = jdbcTemplate.update("DELETE FROM car_shop.car WHERE car_id=?", id);
        if (count == 0) {
            throw new NotFoundException("Car with id " + id + " not found");
        }
    }

    @Override
    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        List<Car> cars = getAll();
        return cars.stream().filter(car -> predicate.test(getter.apply(car))).toList();
    }

}
