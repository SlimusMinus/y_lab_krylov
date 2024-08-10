package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Cars.NOT_EXIST_ID;
import static org.example.dataTest.Orders.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link OrderStorageJdbc}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Testcontainers
@Slf4j
class OrderStorageJdbcTest extends AbstractStorageJdbcTest {
    private OrderStorage storage;

    /**
     * Инициализация {@link OrderStorageJdbc} перед каждым тестом.
     */
    @BeforeEach
    void setUpOrder() {
        storage = new OrderStorageJdbc();
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#create(Order)} для создания нового заказа.
     * Тестирует добавление нового заказа и его правильное сохранение в хранилище.
     */
    @Test
    @DisplayName("Проверка создания нового заказа")
    void create() {
        storage.create(newOrder);
        assertAll(
                () -> assertThat(storage.getAll().size()).isEqualTo(NEW_SIZE),
                () -> assertThat(newOrder).isEqualTo(storage.getAll().get(storage.getAll().size() - 1))
        );
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#getAll()} для получения всех заказов.
     * Тестирует получение всех заказов и их соответствие предустановленному списку.
     */
    @Test
    @DisplayName("Проверка получения всех заказов")
    void getAll() {
        assertAll(
                () -> assertThat(storage.getAll()).isNotNull(),
                () -> assertThat(storage.getAll()).containsAll(listOrder)
        );
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#getById(int)} для получения заказа по ID.
     * Тестирует получение заказа по его идентификатору и проверку его соответствия.
     */
    @Test
    @DisplayName("Проверка получения заказа по ID")
    void getById() {
        Order orderById = storage.getById(order1.getOrderId());
        assertThat(order1).isEqualTo(orderById);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#getById(int)} для получения заказа по ID.
     * Тестирует получение несуществующего заказа по его идентификатору.
     */
    @Test
    @DisplayName("Проверка получения несуществующего заказа по ID")
    void getByIdNotFound(){
        assertThatThrownBy(()->storage.getById(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#changeStatus(int, String)} для изменения статуса заказа.
     * Тестирует изменение статуса заказа и проверку обновленного статуса.
     */
    @Test
    @DisplayName("Проверка изменения статуса заказа")
    void changeStatus() {
        storage.changeStatus(order1.getOrderId(), newStatus);
        assertThat(storage.getById(order1.getOrderId()).getStatus()).isEqualTo(newStatus);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#changeStatus(int, String)} для изменения статуса заказа.
     * Тестирует изменение статуса несуществующего заказа.
     */
    @Test
    @DisplayName("Проверка изменения статуса несуществующего заказа")
    void changeStatusNotFound(){
        assertThatThrownBy(()-> storage.changeStatus(NOT_EXIST_ID, newStatus)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#canceled(int)} для отмены заказа.
     * Тестирует изменение статуса заказа на "отменен" и проверку обновленного статуса.
     */
    @Test
    @DisplayName("Проверка отмены заказа")
    void canceled() {
        storage.canceled(order2.getOrderId());
        assertThat(storage.getById(order2.getOrderId()).getStatus()).isEqualTo(canceledStatus);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#canceled(int)} для отмены заказа.
     * Тестирует изменение статуса несуществующего заказа.
     */
    @Test
    @DisplayName("Проверка отмены несуществующего заказа")
    void canceledNotFound(){
        assertThatThrownBy(()->  storage.canceled(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link OrderStorageJdbc#filter(Function, Predicate)} для фильтрации заказов по дате.
     * Тестирует фильтрацию заказов по дате и проверку результата.
     */
    @Test
    @DisplayName("Проверка фильтрации заказов по дате")
    void filter() {
        final List<Order> filterOrder = storage.filter(Order::getDate, date -> date.isEqual(order1.getDate()));
        assertThat(filterOrder.get(0)).isEqualTo(order1);
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