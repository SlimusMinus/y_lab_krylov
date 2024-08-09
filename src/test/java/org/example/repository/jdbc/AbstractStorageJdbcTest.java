package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@SuppressWarnings("resource")
public abstract class AbstractStorageJdbcTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeEach
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

    abstract String createTable();

    abstract String populateTable();

}
