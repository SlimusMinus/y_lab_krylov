package org.example.repository.inMemory;

import org.example.service.AuthService;
import org.example.repository.UserStorage;
import org.example.service.AuthServiceInMemory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Users.newAdministrator;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Проверка системы на ")
class AuthServiceInMemoryTest {
    private AuthService service = new AuthServiceInMemory();
    private UserStorage userStorage = new UserStorageInMemory();

    @Test
    @DisplayName("регистрацию нового пользователя")
    void registeredUser() {
        service.registeredUser(newAdministrator);
        assertThat(userStorage.getAll().get(userStorage.getAll().size() - 1)).isEqualTo(newAdministrator);
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