package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
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

/**
 * Реализация интерфейса {@link OrderStorage} с использованием JDBC.
 * <p>
 * Этот класс обеспечивает взаимодействие с базой данных для выполнения CRUD операций над сущностью {@link Order}.
 * Он использует {@link JdbcTemplate} и {@link NamedParameterJdbcTemplate} для выполнения SQL-запросов.
 * </p>
 */
@Slf4j
@Repository
public class OrderStorage {
    private static final BeanPropertyRowMapper<Order> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Order.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insert;

    /**
     * Конструктор класса {@link OrderStorage}.
     * <p>
     * Создает экземпляр {@link OrderStorage} с использованием {@link JdbcTemplate} и {@link NamedParameterJdbcTemplate}.
     * </p>
     *
     * @param jdbcTemplate {@link JdbcTemplate} для выполнения SQL-запросов.
     * @param namedParameterJdbcTemplate {@link NamedParameterJdbcTemplate} для выполнения SQL-запросов с именованными параметрами.
     */
    public OrderStorage(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("orders")
                .usingGeneratedKeyColumns("order_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Создает новый заказ в базе данных и устанавливает сгенерированный идентификатор заказа в объекте {@link Order}.
     *
     * @param order Объект {@link Order} для создания в базе данных.
     */
    public void create(Order order) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("user_id", order.getUserId())
                .addValue("car_id", order.getCarId())
                .addValue("date", order.getDate())
                .addValue("status", order.getStatus());
        Number newId = insert.executeAndReturnKey(source);
        order.setOrderId(newId.intValue());
    }

    /**
     * Возвращает список всех заказов из базы данных, отсортированный по идентификатору заказа.
     *
     * @return Список всех заказов {@link Order}.
     */
    public List<Order> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.orders ORDER BY order_id", ROW_MAPPER);
    }

    /**
     * Возвращает заказ по его идентификатору.
     * <p>
     * Если заказ с заданным идентификатором не найден, выбрасывается {@link NotFoundException}.
     * </p>
     *
     * @param id Идентификатор заказа.
     * @return Объект {@link Order} с указанным идентификатором.
     * @throws NotFoundException если заказ с указанным идентификатором не найден.
     */
    public Order getById(int id) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM car_shop.orders where order_id=?", ROW_MAPPER, id);
        Order order = DataAccessUtils.singleResult(orders);
        if (order == null) {
            throw new NotFoundException("Order with id " + id + " not found");
        }
        return order;
    }

    /**
     * Изменяет статус заказа по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @param status Новый статус заказа.
     */
    public void changeStatus(int id, String status) {
        updateOrder(id, status);
    }

    /**
     * Отменяет заказ по его идентификатору, устанавливая статус "canceled".
     *
     * @param id Идентификатор заказа.
     */
    public void canceled(int id) {
        updateOrder(id, "canceled");
    }

    /**
     * Фильтрует заказы по заданному критерию.
     * <p>
     * Этот метод возвращает список заказов, которые соответствуют условию, заданному предикатом {@link Predicate}.
     * </p>
     *
     * @param getter Функция для получения значения, по которому будет проводиться фильтрация.
     * @param predicate Предикат для фильтрации значений.
     * @param <T> Тип значения для фильтрации.
     * @return Список отфильтрованных заказов {@link Order}.
     */
    public <T> List<Order> filter(Function<Order, T> getter, Predicate<T> predicate) {
        log.info("Get all find orders");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    /**
     * Обновляет информацию о заказе по его идентификатору.
     * <p>
     * Метод изменяет статус заказа, а также остальные поля, если они указаны.
     * </p>
     *
     * @param id Идентификатор заказа.
     * @param status Новый статус заказа.
     */
    private void updateOrder(int id, String status) {
        Order updateOrder = getById(id);
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("order_id", id)
                .addValue("user_id", updateOrder.getUserId())
                .addValue("car_id", updateOrder.getCarId())
                .addValue("date", updateOrder.getDate())
                .addValue("status", status);
        if (namedParameterJdbcTemplate.update("UPDATE car_shop.orders set user_id=:user_id, car_id=:car_id," +
                                              "date=:date, status=:status WHERE order_id=:order_id", source) == 0) {
            log.error("Not found order with id {}", id);
            throw new NotFoundException("Id такого пользователя не существует");
        }
    }

}
