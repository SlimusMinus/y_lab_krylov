package org.example.service.inMemory;

import org.example.repository.UserData;
import org.example.model.User;
import org.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 *
 * <p>Этот класс реализует методы для регистрации нового пользователя и аутентификации существующих пользователей.
 * Он использует {@link UserData} для хранения данных о пользователях и ведет логирование с помощью SLF4J.</p>
 */
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    /**
     * Регистрирует нового пользователя в системе.
     *
     * <p>Метод добавляет нового пользователя в хранилище {@link UserData} и присваивает ему уникальный идентификатор.</p>
     *
     * @param user объект {@link User}, содержащий информацию о новом пользователе.
     *             Идентификатор пользователя устанавливается автоматически.
     */
    @Override
    public void registeredUser(User user) {
        log.info("New user is {} added", user);
        int id = UserData.getUserId() + 1;
        user.setId(id);
        UserData.getUsers().put(id, user);
    }

    /**
     * Аутентифицирует пользователя по логину и паролю.
     *
     * <p>Метод проверяет введённые логин и пароль. Если они совпадают с данными пользователя, возвращает его идентификатор.
     * В противном случае возвращает 0, что означает неудачную попытку входа.</p>
     *
     * @param login логин пользователя.
     * @param password пароль пользователя.
     * @return идентификатор пользователя, если аутентификация прошла успешно; 0, если аутентификация не удалась.
     */
    @Override
    public int loginUser(String login, String password) {
        for (var user : UserData.getUsers().values()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                log.info("User {} is logging", user);
                return user.getId();
            }
        }
        log.info("Uncorrected input login {} and password {}", login, password);
        return 0;
    }
}
