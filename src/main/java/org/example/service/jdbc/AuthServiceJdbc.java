package org.example.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Roles;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.UserInformation;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@Slf4j
public class AuthServiceJdbc implements AuthService {

    private Connection connection;

    private final UserInformation userInformation = new UserInformationJdbc();

    public AuthServiceJdbc() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection ", e);
        }
    }

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

    @Override
    public int loginUser(String login, String password) {
        List<User> users = userInformation.getAll();
        for (var user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                log.info("User {} is logging", user);
                return user.getId();
            }
        }
        log.info("Uncorrected input login {} and password {}", login, password);
        return 0;
    }
}
