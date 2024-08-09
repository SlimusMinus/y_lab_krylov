package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.inMemory.data.UserData;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class UserStorageJdbc implements UserStorage {

    private Connection connection;
    public UserStorageJdbc() {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection", e);
        }
    }

    @Override
    public List<User> getAll() {
        List <User> users = new ArrayList<>();
        String query = "SELECT * FROM car_shop.user";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setCity(resultSet.getString("city"));
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("SQL got exception", e);
        }
        return users;
    }

    @Override
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        log.info("Get all users after filter");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    @Override
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        log.info("Get all users after sort");
        return getAll().stream()
                .sorted(Comparator.comparing(keyExtractor)).toList();
    }

    @Override
    public User update(User user) {
        log.info("User {} user was changed", user);
        String query= "UPDATE car_shop.user SET login=?, password=?, name=?, age=?, city=? WHERE user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setInt(4, user.getAge());
            statement.setString(5, user.getCity());
            statement.setInt(6, user.getUserId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
               log.error("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error while updating user: ", e);
        }
        return user;
    }
}
