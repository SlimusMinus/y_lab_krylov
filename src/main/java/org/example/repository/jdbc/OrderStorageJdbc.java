package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.util.NotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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
public class OrderStorageJdbc implements OrderStorage {

    private Connection connection;

    /**
     * Конструктор класса, устанавливающий соединение с базой данных.
     * <p>
     * В случае ошибки при подключении, выбрасывается исключение {@link RuntimeException}.
     * </p>
     */
    public OrderStorageJdbc() throws RuntimeException {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

    /**
     * Создает новый заказ в базе данных.
     *
     * @param order Объект {@link Order}, который нужно создать.
     */
    @Override
    public void create(Order order) {
        String query = "INSERT INTO car_shop.orders (user_id, car_id, date, status) VALUES (?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order.getOrderId());
            statement.setInt(2, order.getCarId());
            statement.setDate(3, Date.valueOf(order.getDate()));
            statement.setString(4, order.getStatus());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error create new order", e);
        }
    }

    /**
     * Возвращает список всех заказов из базы данных.
     *
     * @return Список объектов {@link Order}.
     */
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM car_shop.orders";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Order newOrder = new Order();
                newOrder.setOrderId(resultSet.getInt("order_id"));
                newOrder.setUserId(resultSet.getInt("user_id"));
                newOrder.setCarId(resultSet.getInt("car_id"));
                newOrder.setDate(resultSet.getDate("date").toLocalDate());
                newOrder.setStatus(resultSet.getString("status"));
                orders.add(newOrder);
            }
        } catch (SQLException e) {
            log.error("Error fetching orders", e);
        }
        return orders;
    }

    /**
     * Возвращает заказ из базы данных по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @return Объект {@link Order} с заданным идентификатором, или пустой объект в случае ошибки.
     */
    @Override
    public Order getById(int id) {
        if (id > getAll().size()) {
            log.error("Not found order with id {}", id);
            throw new NotFoundException("Id такого пользователя не существует");
        }
        Order newOrder = new Order();
        String query = "SELECT * FROM car_shop.orders where order_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newOrder.setOrderId(resultSet.getInt("order_id"));
                newOrder.setUserId(resultSet.getInt("user_id"));
                newOrder.setCarId(resultSet.getInt("car_id"));
                newOrder.setDate(resultSet.getDate("date").toLocalDate());
                newOrder.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            log.error("Error fetching orders with id {}", id, e);
        }
        return newOrder;
    }

    /**
     * Обновляет статус заказа в базе данных.
     *
     * @param id     Идентификатор заказа, статус которого нужно обновить.
     * @param status Новый статус заказа.
     */
    @Override
    public void changeStatus(int id, String status) {
        if (id > getAll().size()) {
            log.error("Not found order with id {}", id);
            throw new NotFoundException("Id такого пользователя не существует");
        }
        updateOrder(id, status);
    }

    /**
     * Отмечает заказ как отмененный.
     *
     * @param id Идентификатор заказа, который нужно отменить.
     */
    @Override
    public void canceled(int id) {
        if (id > getAll().size()) {
            log.error("Not found order with id {}", id);
            throw new NotFoundException("Id такого пользователя не существует");
        }
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

    /**
     * Вспомогательный метод для обновления заказа в базе данных.
     *
     * @param id     Идентификатор заказа, который нужно обновить.
     * @param status Новый статус заказа.
     */
    private void updateOrder(int id, String status) {
        Order order = getById(id);
        String query = "UPDATE car_shop.orders SET user_id=?, car_id=?, date=?, status=? WHERE order_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getCarId());
            statement.setDate(3, Date.valueOf(order.getDate()));
            statement.setString(4, status);
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error change status {} orders with id {}", status, id, e);
        }
    }
}
