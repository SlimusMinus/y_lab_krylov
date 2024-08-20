package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.repository.OrderStorage;
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
 * Реализация интерфейса {@link OrderStorage} для управления данными заказов в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для создания, получения, обновления и удаления заказов, а также для фильтрации
 * списка заказов на основе заданных критериев.
 * </p>
 */
@Slf4j
@Repository
public class OrderStorageJdbc implements OrderStorage {
    private static final BeanPropertyRowMapper<Order> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Order.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insert;

    public OrderStorageJdbc(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("orders")
                .usingGeneratedKeyColumns("order_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void create(Order order) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("user_id", order.getUserId())
                .addValue("car_id", order.getCarId())
                .addValue("date", order.getDate())
                .addValue("status", order.getStatus());
        Number newId = insert.executeAndReturnKey(source);
        order.setOrderId(newId.intValue());
    }

    @Override
    public List<Order> getAll() {
        return jdbcTemplate.query("SELECT * FROM car_shop.orders ORDER BY order_id", ROW_MAPPER);
    }

    @Override
    public Order getById(int id) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM car_shop.orders where order_id=?", ROW_MAPPER, id);
        Order order = DataAccessUtils.singleResult(orders);
        if (order == null) {
            throw new NotFoundException("Order with id " + id + " not found");
        }
        return order;
    }

    @Override
    public void changeStatus(int id, String status) {
        updateOrder(id, status);
    }

    @Override
    public void canceled(int id) {
        updateOrder(id, "canceled");
    }

    /**
     * Фильтрует список заказов на основе заданного критерия.
     * <p>
     * Возвращает список заказов, удовлетворяющих заданному условию.
     * </p>
     *
     * @param getter    Функция для получения поля объекта {@link Order}.
     * @param predicate Условие, которое должно быть выполнено для включения заказа в результат.
     * @param <T>       Тип возвращаемого значения функции getter.
     * @return Список объектов {@link Order}, которые соответствуют условию.
     */
    @Override
    public <T> List<Order> filter(Function<Order, T> getter, Predicate<T> predicate) {
        log.info("Get all find orders");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

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
