package org.example.repository;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("resource")

public abstract class AbstractStorageTest {
    /**
     * Контейнер PostgresSQL, используемый для тестирования.
     * Он автоматически запускается и настраивается перед выполнением тестов.
     */
    @Getter
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
        try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
             Statement statement = connection.createStatement()) {
            String createSchemaQuery = "CREATE SCHEMA IF NOT EXISTS car_shop";
            statement.execute(createSchemaQuery);
            statement.execute(createTable());
            statement.execute(populateTable());
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
