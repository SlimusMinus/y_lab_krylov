package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Users.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
@Slf4j
class UserStorageJdbcTest extends AbstractStorageJdbcTest {

    private UserStorage storage;

    @BeforeEach
    void setUpUser() {
        storage = new UserStorageJdbc();
    }

    @Test
    void getAll() {
        assertAll(
                () -> assertThat(storage.getAll()).isNotNull(),
                () -> assertThat(USER_LIST).isEqualTo(storage.getAll())
        );
    }

    @Test
    void filter() {
        assertAll(
                () -> assertThat(storage.filter(User::getName, name -> name.equals("Alexandr"))).isEqualTo(NAME_PHILTER),
                () -> assertThat(storage.filter(User::getCity, city -> city.equals("Moscow"))).isEqualTo(CITY_PHILTER),
                () -> assertThat(storage.filter(User::getAge, age -> age == 33)).isEqualTo(AGE_PHILTER)
        );
    }

    @Test
    void sort() {
        assertAll(
                () -> assertThat(storage.sort(User::getName)).containsAll(USERS_NAME_SORT),
                () -> assertThat(storage.sort(User::getAge)).containsAll(USERS_AGE_SORT)
        );
    }

    @Test
    void edit() {
        storage.update(editClient2);
        assertThat(storage.getAll().get(client2.getUserId())).isEqualTo(editClient2);
    }

    @Override
    String createTable() {
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

    @Override
    String populateTable() {
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