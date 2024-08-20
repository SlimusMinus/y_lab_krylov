package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.util.NotFoundException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link UserStorage} для управления данными пользователей в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для получения, фильтрации, сортировки и обновления данных пользователей в базе данных.
 * </p>
 */

@Repository
@Slf4j
public class UserStorageJdbc implements UserStorage {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public UserStorageJdbc(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.user ORDER BY user_id", ROW_MAPPER);
    }

    @Override
    public User getById(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM car_shop.user where user_id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @Override
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        log.info("Get all users after filter");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    @Override
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        log.info("Get all users after sort");
        return getAll().stream()
                .sorted(Comparator.comparing(keyExtractor)).toList();
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("user_id", user.getUserId())
                .addValue("login", user.getLogin())
                .addValue("password", user.getPassword())
                .addValue("name", user.getName())
                .addValue("age", user.getAge())
                .addValue("city", user.getCity());
        if (namedParameterJdbcTemplate.update("UPDATE car_shop.user set login=:login, password=:password, name=:name," +
                                              "age=:age, city=:city WHERE user_id=:user_id", map) == 0) {
            throw new NotFoundException("User with {} id not found");
        }
        return user;
    }

}
