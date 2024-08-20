package org.example.service.authentication;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Roles;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
@Service
public class AuthServiceJdbc implements AuthService {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userInsert;
    private final SimpleJdbcInsert roleInsert;

    @Autowired
    private UserStorage userStorage;

    public AuthServiceJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("user")
                .usingGeneratedKeyColumns("user_id");
        this.roleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("car_shop")
                .withTableName("user_roles")
                .usingGeneratedKeyColumns("role_id");
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
        SqlParameterSource userParams = new MapSqlParameterSource()
                .addValue("login", user.getLogin())
                .addValue("password", user.getPassword())
                .addValue("name", user.getName())
                .addValue("age", user.getAge())
                .addValue("city", user.getCity());
        Number userId = userInsert.executeAndReturnKey(userParams);
        SqlParameterSource[] rolesParams = user.getRole().stream()
                .map(role -> new MapSqlParameterSource()
                        .addValue("user_id", userId.intValue())
                        .addValue("role", role.getTitle()))
                .toArray(SqlParameterSource[]::new);
        roleInsert.executeBatch(rolesParams);
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
