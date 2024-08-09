package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Order;
import org.example.repository.OrderStorage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class OrderStorageJdbc implements OrderStorage {

    private Connection connection;

    public OrderStorageJdbc() throws RuntimeException {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

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

    @Override
    public Order getById(int id) {
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

    @Override
    public void changeStatus(int id, String status) {
        updateOrder(id, status);
    }

    @Override
    public void canceled(int id) {
        updateOrder(id, "canceled");
    }

    @Override
    public <T> List<Order> filter(Function<Order, T> getter, Predicate<T> predicate) {
        log.info("Get all find orders");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

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
