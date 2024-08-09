package org.example.repository.inMemory;

import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.inMemory.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Users.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Тестирование класса сервиса пользователей на ")
class UserStorageInMemoryTest {
    UserStorage userStorage;

    @BeforeEach
    public void setUp(){
        UserData.setUsers(testUsers);
        userStorage = new UserStorageInMemory();
    }

    @Test
    @DisplayName("получение всего списка пользователей")
    void getAll() {
        assertAll(
                () -> assertThat(userStorage.getAll()).isNotNull(),
                () -> assertThat(userStorage.getAll()).isEqualTo(USER_LIST)
        );
    }

    @Test
    @DisplayName("фильтрации пользователей")
    void filter() {
        assertAll(
                () -> assertThat(userStorage.filter(User::getName, name -> name.equals("Alexandr"))).isEqualTo(NAME_PHILTER),
                () -> assertThat(userStorage.filter(User::getCity, city -> city.equals("Moscow"))).isEqualTo(CITY_PHILTER),
                () -> assertThat(userStorage.filter(User::getAge, age -> age == 33)).isEqualTo(AGE_PHILTER)
        );
    }

    @Test
    @DisplayName("изменение одного пользователя из списка")
    void edit() {
        userStorage.update(editClient2);
        assertThat(userStorage.getAll().get(client2.getUserId() - 1)).isEqualTo(editClient2);
    }
}