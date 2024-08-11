package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Roles;
import org.example.repository.RoleStorage;
import org.example.repository.UserStorage;
import org.example.repository.inMemory.UserStorageInMemory;
import org.example.repository.jdbc.AbstractStorageJdbcTest;
import org.example.repository.jdbc.RoleStorageJdbc;
import org.example.repository.jdbc.UserStorageJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Users.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки работы сервиса аутентификации {@code AuthServiceJdbc}.
 * <p>
 * Данный класс тестирует методы регистрации пользователей, входа в систему и получения ролей пользователей.
 * Он наследуется от {@code AbstractStorageJdbcTest}, который предоставляет базовые методы для управления
 * тестовой базой данных.
 * </p>
 */
@Testcontainers
@Slf4j
@DisplayName("Тестирование класса AuthServiceJdbc")
class AuthServiceJdbcTest extends AbstractStorageJdbcTest {

    private AuthService authService;
    private UserStorage userStorage;
    private RoleStorage roleStorage;

    /**
     * Инициализация сервиса аутентификации и зависимостей перед каждым тестом.
     */
    @BeforeEach
    @DisplayName("Инициализация сервиса и хранилищ перед тестом")
    void setUpService() {
        authService = new AuthServiceJdbc();
        userStorage = new UserStorageJdbc();
        roleStorage = new RoleStorageJdbc();
    }

    /**
     * Тест метода регистрации пользователя.
     * <p>
     * Проверяет, что после регистрации нового пользователя, он правильно сохраняется в хранилище пользователей.
     * </p>
     */
    @Test
    @DisplayName("Регистрация нового пользователя")
    void registeredUser() {
        authService.registeredUser(newAdministrator);
        assertThat(userStorage.getAll().get(userStorage.getAll().size() - 1)).isEqualTo(newUser);
    }

    /**
     * Тест метода входа пользователя в систему.
     * <p>
     * Проверяет корректность входа для существующих и несуществующих пользователей.
     * </p>
     */
    @Test
    @DisplayName("Авторизация пользователя в системе")
    void loginUser() {
        assertAll(
                () -> assertThat(authService.loginUser("admin", "admin")).isEqualTo(1),
                () -> assertThat(authService.loginUser("client1", "client1")).isEqualTo(4),
                () -> assertThat(authService.loginUser("qwerty", "qwerty")).isEqualTo(0)
        );
    }

    /**
     * Тест метода получения ролей пользователя по его идентификатору.
     * <p>
     * Проверяет, что после регистрации нового пользователя, его роли корректно сохраняются и могут быть получены.
     * </p>
     */
    @Test
    @DisplayName("Получение ролей пользователя по идентификатору")
    void getRolesById() {
        assertThat(roles).isEqualTo(roleStorage.getById(1));
    }


    /**
     * Создает таблицы в тестовой базе данных.
     *
     * @return SQL-запрос для создания таблиц.
     */
    @Override
    @DisplayName("Создание таблиц базы данных")
    protected String createTable() {
        return """
                DROP TABLE IF EXISTS car_shop.user;
                CREATE TABLE car_shop.user(
                user_id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT,
                name TEXT,
                age INTEGER,
                city TEXT);
                DROP TABLE IF EXISTS car_shop.user_roles;
                CREATE TABLE car_shop.user_roles(
                role_id SERIAL PRIMARY KEY,
                user_id INTEGER,
                role TEXT
                )
                """;
    }

    /**
     * Заполняет таблицы тестовыми данными.
     *
     * @return SQL-запрос для заполнения таблиц тестовыми данными.
     */
    @Override
    @DisplayName("Заполнение таблиц базы данных тестовыми данными")
    protected String populateTable() {
        return """
                INSERT INTO car_shop.user (login,password,name,age,city) VALUES
                ('admin','admin','Alexandr',33,'Moscow'),
                ('manager1','manager1','John',36,'New-York'),
                ('manager2','manager2','Alexandr',34,'Moscow'),
                ('client1','client1','Tanya',25,'London'),
                ('client2','client2','Valera',45,'Milan'),
                ('client3','client3','Robert',33,'Moscow');
                INSERT INTO car_shop.user_roles (user_id, role) VALUES 
                (1, 'Администратор'),
                (1, 'Менеджер')
                """;
    }
}