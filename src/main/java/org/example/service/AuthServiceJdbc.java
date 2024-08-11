package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Roles;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;

import java.io.IOException;
import java.sql.*;

/**
 * Реализация интерфейса {@link AuthService}, обеспечивающая авторизацию и регистрацию пользователей в базе данных с использованием JDBC.
 * <p>
 * Класс использует подключение к базе данных для выполнения операций регистрации и входа пользователей.
 * <p>
 * Основные методы включают:
 * <ul>
 *   <li>{@link #registeredUser(User)} - Регистрация нового пользователя в системе, включая сохранение его данных и ролей.</li>
 *   <li>{@link #loginUser(String, String)} - Авторизация пользователя на основе логина и пароля.</li>
 * </ul>
 * <p>
 * Класс также использует аннотацию Lombok {@link Slf4j} для логирования операций.
 * <p>
 * При создании экземпляра класса устанавливается подключение к базе данных с использованием настроек из {@link DatabaseConfig}.
 * Если возникает ошибка при подключении, она логируется.
 *
 * @see AuthService
 * @see UserStorageJdbc
 */
@Slf4j
public class AuthServiceJdbc implements AuthService {

    private Connection connection;

    private final UserStorage userStorage = new UserStorageJdbc();

    /**
     * Конструктор класса. Устанавливает подключение к базе данных.
     * <p>
     * Если происходит ошибка при получении подключения, она логируется.
     */
    public AuthServiceJdbc() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

    /**
     * Регистрирует нового пользователя в системе.
     * <p>
     * В базу данных сохраняются логин, пароль, имя, возраст и город пользователя.
     * Также сохраняются роли пользователя, которые связываются с его идентификатором.
     *
     * @param user Объект {@link User}, содержащий данные пользователя и его роли.
     */
    @Override
    public void registeredUser(User user) {
        String queryUser = "INSERT INTO car_shop.user (login, password, name, age, city) VALUES (?,?,?,?,?)";
        String queryRole = "INSERT INTO car_shop.user_roles (user_id, role) VALUES (?,?)";
        try (PreparedStatement userStatement = connection.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS)) {
            userStatement.setString(1, user.getLogin());
            userStatement.setString(2, user.getPassword());
            userStatement.setString(3, user.getName());
            userStatement.setInt(4, user.getAge());
            userStatement.setString(5, user.getCity());
            userStatement.executeUpdate();

            ResultSet generatedKeys = userStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                try (PreparedStatement roleStatement = connection.prepareStatement(queryRole)) {
                    for (Roles role : user.getRole()) {
                        roleStatement.setInt(1, userId);
                        roleStatement.setString(2, role.getTitle());
                        roleStatement.addBatch();
                    }
                    roleStatement.executeBatch();
                }
            }

        } catch (SQLException e) {
            log.error("Error adding user {}", user, e);
        }
    }

    /**
     * Проверяет логин и пароль пользователя для авторизации.
     * <p>
     * Если пользователь найден и данные совпадают, возвращается его идентификатор.
     * В противном случае возвращается 0.
     *
     * @param login    Логин пользователя.
     * @param password Пароль пользователя.
     * @return Идентификатор пользователя при успешной авторизации или 0 при неудачной попытке.
     */
    @Override
    public int loginUser(String login, String password) {
        for (var user : userStorage.getAll()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                log.info("User {} is logging", user);
                return user.getUserId();
            }
        }
        log.info("Uncorrected input login {} and password {}", login, password);
        return 0;
    }
}
