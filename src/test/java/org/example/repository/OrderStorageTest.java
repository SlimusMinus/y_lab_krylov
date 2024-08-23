package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.AppConfigTest;
import org.example.model.Order;
import org.example.service.OrderService;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Cars.NOT_EXIST_ID;
import static org.example.dataTest.Orders.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link OrderStorage}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Testcontainers
@Slf4j
@SpringJUnitConfig(AppConfigTest.class)
@DisplayName("Тестирование класса OrderStorage")
class OrderStorageTest extends AbstractStorageTest {

    @Autowired
    @Qualifier("orderServiceTest")
    private OrderService service;

    @Test
    @DisplayName("Проверка создания нового заказа")
    void create() {
        service.create(newOrder);
        assertAll(
                () -> assertThat(service.getAll().size()).isEqualTo(NEW_SIZE),
                () -> assertThat(newOrder).isEqualTo(service.getById(service.getAll().size()))
        );
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#getAll()} для получения всех заказов.
     * Тестирует получение всех заказов и их соответствие предустановленному списку.
     */
    @Test
    @DisplayName("Проверка получения всех заказов")
    void getAll() {
        assertAll(
                () -> assertThat(service.getAll()).isNotNull(),
                () -> assertThat(service.getAll()).containsAll(allListOrder)
        );
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#getById(int)} для получения заказа по ID.
     * Тестирует получение заказа по его идентификатору и проверку его соответствия.
     */
    @Test
    @DisplayName("Проверка получения заказа по ID")
    void getById() {
        Order orderById = service.getById(order1.getOrderId());
        assertThat(orderById).isEqualTo(orderById);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#getById(int)} для получения заказа по ID.
     * Тестирует получение несуществующего заказа по его идентификатору.
     */
    @Test
    @DisplayName("Проверка получения несуществующего заказа по ID")
    void getByIdNotFound() {
        assertThatThrownBy(() -> service.getById(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#changeStatus(int, String)} для изменения статуса заказа.
     * Тестирует изменение статуса заказа и проверку обновленного статуса.
     */
    @Test
    @DisplayName("Проверка изменения статуса заказа")
    void changeStatus() {
        service.changeStatus(order1.getOrderId(), newStatus);
        assertThat(service.getById(order1.getOrderId()).getStatus()).isEqualTo(newStatus);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#changeStatus(int, String)} для изменения статуса заказа.
     * Тестирует изменение статуса несуществующего заказа.
     */
    @Test
    @DisplayName("Проверка изменения статуса несуществующего заказа")
    void changeStatusNotFound() {
        assertThatThrownBy(() -> service.changeStatus(NOT_EXIST_ID, newStatus)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#canceled(int)} для отмены заказа.
     * Тестирует изменение статуса заказа на "отменен" и проверку обновленного статуса.
     */
    @Test
    @DisplayName("Проверка отмены заказа")
    void canceled() {
        service.canceled(order2.getOrderId());
        assertThat(service.getById(order2.getOrderId()).getStatus()).isEqualTo(canceledStatus);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#canceled(int)} для отмены заказа.
     * Тестирует изменение статуса несуществующего заказа.
     */
    @Test
    @DisplayName("Проверка отмены несуществующего заказа")
    void canceledNotFound() {
        assertThatThrownBy(() -> service.canceled(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorage#filter(Function, Predicate)} для фильтрации заказов по дате.
     * Тестирует фильтрацию заказов по дате и проверку результата.
     */
    @Test
    @DisplayName("Проверка фильтрации заказов по дате")
    void filter() {
        final List<Order> filterOrders = service.getFilteredOrder("date", order1.getDate().toString());
        assertThat(filterOrders).isEqualTo(List.of(filterOrder));
    }

    /**
     * Скрипт для создания таблицы заказов в testContainer
     */
    @Override
    protected String createTable() {
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

    /**
     * Скрипт для заполнения таблицы заказов в testContainer
     */
    @Override
    protected String populateTable() {
        return """
                INSERT INTO car_shop.orders (user_id, car_id, date, status) VALUES
                (4,1,'2024-08-12','заказ оформлен'),
                (4,2,'2024-08-13','готов к выдаче')
                """;
    }
}