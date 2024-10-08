package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.AppConfigTest;
import org.example.dataTest.Users;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Users.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link UserService}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Testcontainers
@Slf4j
@SpringJUnitConfig(AppConfigTest.class)
@DisplayName("Тестирование класса UserService")
public class UserStorageTest extends AbstractStorageTest {

    @Autowired
    @Qualifier("userServiceTest")
    private UserService service;

    /**
     * Проверяет корректность работы метода {@link UserService#getAll()} для получения всех пользователей.
     * Тестирует получение всех пользователей и их соответствие предустановленному списку {@link Users#USER_LIST}.
     */
    @Test
    @DisplayName("Проверка получения всех пользователей")
    void getAll() {
        assertAll(
                () -> assertThat(service.getAll()).isNotNull(),
                () -> assertThat(USER_LIST).isEqualTo(service.getAll())
        );
    }

    /**
     * Тест проверяет корректность работы метода {@link UserService#getById(int)}.
     * <p>
     * Данный тест вызывает метод {@code getById} для получения пользователя по заданному идентификатору
     * и сравнивает полученный объект {@link User} с ожидаемым объектом {@code manager1}.
     * Если возвращаемый методами объект соответствует ожидаемому, тест считается успешным.
     * </p>
     */
    @Test
    @DisplayName("Тест на получение пользователя по идентификатору")
    void getById() {
        User user = service.getById(USER_GET_ID);
        assertThat(manager1).isEqualTo(user);
    }

    /**
     * Тест проверяет, что метод {@link UserService#getById(int)} выбрасывает исключение {@link NotFoundException},
     * если вызывается с идентификатором пользователя, который не существует в базе данных.
     * <p>
     * В данном тесте вызывается метод {@code getById} с несуществующим идентификатором {@code NOT_EXIST_ID}.
     * Тест считается успешным, если метод выбрасывает исключение {@link NotFoundException}.
     * </p>
     */
    @Test
    @DisplayName("Тест на выброс NotFoundException при попытке получить несуществующего пользователя")
    void getByIdNotFound() {
        assertThatThrownBy(() -> service.getById(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link UserService#getFilteredUsers(String, String)} для фильтрации пользователей.
     * Тестирует фильтрацию пользователей по имени, городу и возрасту, а также проверку результатов фильтрации.
     */
    @Test
    @DisplayName("Проверка фильтрации пользователей по критериям")
    void filter() {
        assertAll(
                () -> assertThat(service.getFilteredUsers("name", "Alexandr")).isEqualTo(NAME_PHILTER),
                () -> assertThat(service.getFilteredUsers("city", "Moscow")).isEqualTo(CITY_PHILTER),
                () -> assertThat(service.getFilteredUsers("age", "33")).isEqualTo(AGE_PHILTER)
        );
    }

    /**
     * Проверяет корректность работы метода {@link UserService#getSortedUsers(String)} (Function)} для сортировки пользователей.
     * Тестирует сортировку пользователей по имени и возрасту, а также проверку результатов сортировки.
     */
    @Test
    @DisplayName("Проверка сортировки пользователей по критериям")
    void sort() {
        assertAll(
                () -> assertThat(service.getSortedUsers("name")).containsAll(USERS_NAME_SORT),
                () -> assertThat(service.getSortedUsers("age")).containsAll(USERS_AGE_SORT)
        );
    }

    /**
     * Проверяет корректность работы метода {@link UserService#update(User)} для обновления данных пользователя.
     * Тестирует обновление данных пользователя и проверку, что обновленный пользователь корректно сохранен в хранилище.
     */
    @Test
    @DisplayName("Проверка обновления данных пользователя")
    void update() {
        service.update(editClient2);
        assertThat(service.getById(client2.getUserId())).isEqualTo(editClient2);
    }

    /**
     * Скрипт для создания таблицы пользователей в testContainer
     */
    @Override
    protected String createTable() {
        return """
                DROP TABLE IF EXISTS car_shop.user;
                CREATE TABLE car_shop.user(
                user_id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT,
                name TEXT,
                age INTEGER,
                city TEXT)""";
    }

    /**
     * Скрипт для заполнения таблицы пользователей в testContainer
     */
    @Override
    protected String populateTable() {
        return """
                INSERT INTO car_shop.user (login,password,name,age,city) VALUES
                ('admin','admin','Alexandr',33,'Moscow'),
                ('manager1','manager1','John',36,'New-York'),
                ('manager2','manager2','Alexandr',34,'Moscow'),
                ('client1','client1','Tanya',25,'London'),
                ('client2','client2','Valera',45,'Milan'),
                ('client3','client3','Robert',33,'Moscow')
                """;
    }
}
