package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Orders.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
@Slf4j
class OrderStorageJdbcTest extends AbstractStorageJdbcTest {
    private OrderStorage storage;

    @BeforeEach
    void setUpOrder() {
        storage = new OrderStorageJdbc();
    }

    @Test
    void create() {
        storage.create(newOrder);
        assertAll(
                () -> assertThat(storage.getAll().size()).isEqualTo(NEW_SIZE),
                () -> assertThat(newOrder).isEqualTo(storage.getAll().get(storage.getAll().size() - 1))
        );
    }

    @Test
    void getAll() {
        assertAll(
                () -> assertThat(storage.getAll()).isNotNull(),
                () -> assertThat(storage.getAll()).containsAll(listOrder)
        );
    }

    @Test
    void getById() {
        Order orderById = storage.getById(order1.getOrderId());
        assertThat(order1).isEqualTo(orderById);
    }

    @Test
    void changeStatus() {
        storage.changeStatus(order1.getOrderId(), newStatus);
        assertThat(storage.getById(order1.getOrderId()).getStatus()).isEqualTo(newStatus);
    }

    @Test
    void canceled() {
        storage.canceled(order2.getOrderId());
        assertThat(storage.getById(order2.getOrderId()).getStatus()).isEqualTo(canceledStatus);
    }

    @Test
    void filter() {
        final List<Order> filterOrder = storage.filter(Order::getDate, date -> date.isEqual(order1.getDate()));
        assertThat(filterOrder.get(0)).isEqualTo(order1);
    }

    @Override
    String createTable() {
        return """
                    DROP TABLE IF EXISTS car_shop.orders;
                    CREATE TABLE car_shop.orders(
                        order_id SERIAL PRIMARY KEY,
                        user_id INTEGER,
                        car_id INTEGER,
                        date DATE,
                        status TEXT
                    );
                    """;
    }

    @Override
    String populateTable() {
        return """
                    INSERT INTO car_shop.orders (user_id, car_id, date, status) VALUES
                    (4,1,'2024-08-12','заказ оформлен'),
                    (4,2,'2024-08-13','готов к выдаче')
                    """;
    }
}