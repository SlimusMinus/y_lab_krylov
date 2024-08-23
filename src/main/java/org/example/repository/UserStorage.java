package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.util.NotFoundException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link UserStorage} с использованием JDBC.
 * <p>
 * Этот класс обеспечивает взаимодействие с базой данных для выполнения CRUD операций над сущностью {@link User}.
 * Он использует {@link JdbcTemplate} и {@link NamedParameterJdbcTemplate} для выполнения SQL-запросов.
 * </p>
 */
@Repository
@Slf4j
public class UserStorage {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Конструктор класса {@link UserStorage}.
     * <p>
     * Создает экземпляр {@link UserStorage} с использованием {@link JdbcTemplate} и {@link NamedParameterJdbcTemplate}.
     * </p>
     *
     * @param jdbcTemplate {@link JdbcTemplate} для выполнения SQL-запросов.
     * @param namedParameterJdbcTemplate {@link NamedParameterJdbcTemplate} для выполнения SQL-запросов с именованными параметрами.
     */
    public UserStorage(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Возвращает список всех пользователей из базы данных, отсортированный по идентификатору пользователя.
     *
     * @return Список всех пользователей {@link User}.
     */
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.user ORDER BY user_id", ROW_MAPPER);
    }

    /**
     * Возвращает пользователя по его идентификатору.
     * <p>
     * Если пользователь с заданным идентификатором не найден, выбрасывается {@link NotFoundException}.
     * </p>
     *
     * @param id Идентификатор пользователя.
     * @return Объект {@link User} с указанным идентификатором.
     * @throws NotFoundException если пользователь с указанным идентификатором не найден.
     */
    public User getById(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM car_shop.user where user_id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    /**
     * Фильтрует пользователей по заданному критерию.
     * <p>
     * Этот метод возвращает список пользователей, которые соответствуют условию, заданному предикатом {@link Predicate}.
     * </p>
     *
     * @param getter Функция для получения значения, по которому будет проводиться фильтрация.
     * @param predicate Предикат для фильтрации значений.
     * @param <T> Тип значения для фильтрации.
     * @return Список отфильтрованных пользователей {@link User}.
     */
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        log.info("Get all users after filter");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    /**
     * Сортирует пользователей по заданному критерию.
     * <p>
     * Этот метод возвращает список пользователей, отсортированный по значению, полученному с помощью функции {@link Function}.
     * </p>
     *
     * @param keyExtractor Функция для получения ключа для сортировки.
     * @param <T> Тип ключа для сортировки.
     * @return Список отсортированных пользователей {@link User}.
     */
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        log.info("Get all users after sort");
        return getAll().stream()
                .sorted(Comparator.comparing(keyExtractor)).toList();
    }

    /**
     * Обновляет информацию о пользователе.
     * <p>
     * Если пользователь с указанным идентификатором не найден, выбрасывается {@link NotFoundException}.
     * </p>
     *
     * @param user Объект {@link User} с обновленной информацией.
     * @return Обновленный объект {@link User}.
     * @throws NotFoundException если пользователь с указанным идентификатором не найден.
     */
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
