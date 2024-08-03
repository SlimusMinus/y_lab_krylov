package org.example.service;

import org.example.model.Order;
import org.example.repository.OrderData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.DataTest.Orders.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Проверка списка заказов на ")
class OrderManagementImplTest {

    private OrderManagement management;

    @BeforeEach
    void setUp(){
        management = new OrderManagementImpl();
    }

    @AfterEach
    void updateAllOrders(){
        OrderData.setOrders(orders);
    }

    @Test
    @DisplayName("добавление нового заказа")
    void create() {
        management.create(newOrder);
        assertThat(newOrder).isEqualTo(management.getAll().get(management.getAll().size()-1));
    }

    @Test
    @DisplayName("получения списка всех заказов")
    void getAll() {
        assertAll(
                ()->assertThat(management.getAll()).isNotNull(),
                ()->assertThat(management.getAll()).containsAll(listOrder)
        );
    }

    @Test
    @DisplayName("получение заказа по id")
    void getById() {
        Order orderById = management.getById(order1.getOrderId());
        assertThat(order1).isEqualTo(orderById);
    }

    @Test
    @DisplayName("изменение статуса заказа")
    void changeStatus() {
        management.changeStatus(order1.getOrderId(), newStatus);
        assertThat(management.getById(order1.getOrderId()).getStatus()).isEqualTo(newStatus);
    }

    @Test
    @DisplayName("отмену заказа")
    void canceled() {
        management.canceled(order2.getOrderId());
        assertThat(OrderData.getOrders()).doesNotContainKey(order2.getOrderId());
    }

    @Test
    @DisplayName("фильтрацию заказов")
    void filter() {
        final List<Order> filterOrder = management.filter(Order::getDate, date -> date.isEqual(order1.getDate()));
        assertThat(filterOrder.get(0)).isEqualTo(order1);
    }
}