package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.dataTest.Users;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.dataTest.Users.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Тестовый класс для проверки функциональности {@link UserStorageJdbc}.
 * Этот класс использует контейнер PostgreSQL для выполнения интеграционных тестов.
 */
@Testcontainers
@Slf4j
@DisplayName("Тестирование класса UserStorageJdbc")
class UserStorageJdbcTest extends AbstractStorageJdbcTest {

    private UserStorage storage;

    /**
     * Инициализация {@link UserStorageJdbc} перед каждым тестом.
     */
    @BeforeEach
    void setUpUser() {
        storage = new UserStorageJdbc();
    }

    /**
     * Проверяет корректность работы метода {@link UserStorageJdbc#getAll()} для получения всех пользователей.
     * Тестирует получение всех пользователей и их соответствие предустановленному списку {@link Users#USER_LIST}.
     */
    @Test
    @DisplayName("Проверка получения всех пользователей")
    void getAll() {
        assertAll(
                () -> assertThat(storage.getAll()).isNotNull(),
                () -> assertThat(USER_LIST).isEqualTo(storage.getAll())
        );
    }

    /**
     * Тест проверяет корректность работы метода {@link UserStorage#getById(int)}.
     * <p>
     * Данный тест вызывает метод {@code getById} для получения пользователя по заданному идентификатору
     * и сравнивает полученный объект {@link User} с ожидаемым объектом {@code manager1}.
     * Если возвращаемый методами объект соответствует ожидаемому, тест считается успешным.
     * </p>
     */
    @Test
    @DisplayName("Тест на получение пользователя по идентификатору")
    void getById(){
        User user = storage.getById(USER_GET_ID);
        assertThat(manager1).isEqualTo(user);
    }

    /**
     * Тест проверяет, что метод {@link UserStorage#getById(int)} выбрасывает исключение {@link NotFoundException},
     * если вызывается с идентификатором пользователя, который не существует в базе данных.
     * <p>
     * В данном тесте вызывается метод {@code getById} с несуществующим идентификатором {@code NOT_EXIST_ID}.
     * Тест считается успешным, если метод выбрасывает исключение {@link NotFoundException}.
     * </p>
     */
    @Test
    @DisplayName("Тест на выброс NotFoundException при попытке получить несуществующего пользователя")
    void getByIdNotFound(){
        assertThatThrownBy(()->storage.getById(NOT_EXIST_ID)).isInstanceOf(NotFoundException.class);
    }

    /**
     * Проверяет корректность работы метода {@link UserStorageJdbc#filter(Function, Predicate)} для фильтрации пользователей.
     * Тестирует фильтрацию пользователей по имени, городу и возрасту, а также проверку результатов фильтрации.
     */
    @Test
    @DisplayName("Проверка фильтрации пользователей по критериям")
    void filter() {
        assertAll(
                () -> assertThat(storage.filter(User::getName, name -> name.equals("Alexandr"))).isEqualTo(NAME_PHILTER),
                () -> assertThat(storage.filter(User::getCity, city -> city.equals("Moscow"))).isEqualTo(CITY_PHILTER),
                () -> assertThat(storage.filter(User::getAge, age -> age == 33)).isEqualTo(AGE_PHILTER)
        );
    }

    /**
     * Проверяет корректность работы метода {@link UserStorageJdbc#sort(Function)} для сортировки пользователей.
     * Тестирует сортировку пользователей по имени и возрасту, а также проверку результатов сортировки.
     */
    @Test
    @DisplayName("Проверка сортировки пользователей по критериям")
    void sort() {
        assertAll(
                () -> assertThat(storage.sort(User::getName)).containsAll(USERS_NAME_SORT),
                () -> assertThat(storage.sort(User::getAge)).containsAll(USERS_AGE_SORT)
        );
    }

    /**
     * Проверяет корректность работы метода {@link UserStorageJdbc#update(User)} для обновления данных пользователя.
     * Тестирует обновление данных пользователя и проверку, что обновленный пользователь корректно сохранен в хранилище.
     */
    @Test
    @DisplayName("Проверка обновления данных пользователя")
    void update() {
        storage.update(editClient2);
        assertThat(storage.getAll().get(client2.getUserId())).isEqualTo(editClient2);
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