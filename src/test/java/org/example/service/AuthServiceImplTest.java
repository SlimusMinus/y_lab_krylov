package org.example.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.DataTest.Users.newAdministrator;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Проверка системы на ")
class AuthServiceImplTest {
    private AuthService service = new AuthServiceImpl();
    private UserInformation userInformation = new UserInformationImpl();

    @Test
    @DisplayName("регистрацию нового пользователя")
    void registeredUser() {
        service.registeredUser(newAdministrator);
        assertThat(userInformation.getAll().get(userInformation.getAll().size() - 1)).isEqualTo(newAdministrator);
    }

    @Test
    @DisplayName("аутентификацию зарегистрированного пользователя")
    void loginUser() {
        assertAll(
                () -> assertThat(service.loginUser("admin", "admin")).isEqualTo(1),
                () -> assertThat(service.loginUser("client1", "client1")).isEqualTo(4),
                () -> assertThat(service.loginUser("qwerty", "qwerty")).isEqualTo(0)
        );

    }
}