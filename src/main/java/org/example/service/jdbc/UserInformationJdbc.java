package org.example.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.User;
import org.example.service.UserInformation;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class UserInformationJdbc implements UserInformation {

    private Connection connection;
    public UserInformationJdbc() {
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
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setCity("city");
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        return null;
    }

    @Override
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        return null;
    }

    @Override
    public User edit(User user) {
        return null;
    }
}
