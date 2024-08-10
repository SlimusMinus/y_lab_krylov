package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Абстрактный класс для настройки и управления тестовой базой данных PostgresSQL с использованием Testcontainers.
 * <p>
 * Данный класс предоставляет инфраструктуру для тестов, работающих с базой данных. Он автоматически поднимает
 * контейнер PostgresSQL, создает схему, таблицы, и заполняет их тестовыми данными перед каждым тестом.
 * </p>
 */
@Slf4j
@SuppressWarnings("resource")
public abstract class AbstractStorageJdbcTest {
    /**
     * Контейнер PostgresSQL, используемый для тестирования.
     * Он автоматически запускается и настраивается перед выполнением тестов.
     */
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    /**
     * Метод, выполняющий начальную настройку перед каждым тестом.
     * <p>
     * Этот метод устанавливает соединение с базой данных, создает схему, таблицы, и заполняет их тестовыми данными.
     * </p>
     */
    @BeforeEach
    @DisplayName("Создание схемы и таблиц базы данных, а также их заполнение перед тестом")
    void setUp() {
        DatabaseConfig.setConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String createSchemaQuery = "CREATE SCHEMA IF NOT EXISTS car_shop";
            statement.execute(createSchemaQuery);
            statement.execute(createTable());
            statement.execute(populateTable());
        } catch (SQLException | IOException e) {
            log.error("SQL got exception", e);
        }
    }

    /**
     * Абстрактный метод для создания таблиц в базе данных.
     * <p>
     * Этот метод должен быть реализован в дочерних классах для определения SQL-запроса на создание таблиц.
     * </p>
     *
     * @return SQL-запрос для создания таблиц.
     */
    @DisplayName("Создание таблиц базы данных")
    protected abstract String createTable();

    /**
     * Абстрактный метод для заполнения таблиц базы данных тестовыми данными.
     * <p>
     * Этот метод должен быть реализован в дочерних классах для определения SQL-запроса на вставку тестовых данных.
     * </p>
     *
     * @return SQL-запрос для заполнения таблиц тестовыми данными.
     */
    @DisplayName("Заполнение таблиц базы данных тестовыми данными")
    protected abstract String populateTable();

}
