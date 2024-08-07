package org.example.service;

import org.example.model.User;
import org.example.repository.UserData;
import org.example.service.inMemory.UserInformationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.DataTest.Users.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Тестирование класса сервиса пользователей на ")
class UserInformationImplTest {
    UserInformation userInformation;

    @BeforeEach
    public void setUp(){
        UserData.setUsers(testUsers);
        userInformation = new UserInformationImpl();
    }

    @Test
    @DisplayName("получение всего списка пользователей")
    void getAll() {
        assertAll(
                () -> assertThat(userInformation.getAll()).isNotNull(),
                () -> assertThat(userInformation.getAll()).isEqualTo(userList)
        );
    }

    @Test
    @DisplayName("фильтрации пользователей")
    void filter() {
        assertAll(
                () -> assertThat(userInformation.filter(User::getName, name -> name.equals("Alexandr"))).isEqualTo(namePhilter),
                () -> assertThat(userInformation.filter(User::getCity, city -> city.equals("Moscow"))).isEqualTo(cityPhilter),
                () -> assertThat(userInformation.filter(User::getAge, age -> age == 33)).isEqualTo(agePhilter)
        );
    }

    @Test
    @DisplayName("изменение одного пользователя из списка")
    void edit() {
        userInformation.edit(editClient2);
        assertThat(userInformation.getAll().get(client2.getId() - 1)).isEqualTo(editClient2);
    }
}