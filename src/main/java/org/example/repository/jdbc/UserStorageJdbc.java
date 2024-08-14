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

/**
 * Реализация интерфейса {@link UserStorage} для управления данными пользователей в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для получения, фильтрации, сортировки и обновления данных пользователей в базе данных.
 * </p>
 */
@Slf4j
public class UserStorageJdbc implements UserStorage, AutoCloseable {

    private Connection connection;

    /**
     * Конструктор класса, устанавливающий соединение с базой данных.
     * <p>
     * В случае ошибки при подключении, выбрасывается исключение {@link RuntimeException}.
     * </p>
     */
    public UserStorageJdbc() {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection", e);
        }
    }

    /**
     * Закрывает соединение с базой данных.
     * <p>
     * Если соединение с базой данных было установлено (не равно {@code null}),
     * метод пытается его закрыть. В случае успешного закрытия в журнал записывается
     * информационное сообщение. Если возникает ошибка при закрытии соединения,
     * она записывается в журнал как ошибка.
     * </p>
     *
     * @throws SQLException Если произошла ошибка при закрытии соединения.
     */
    @Override
    public void close() throws Exception {
        if (connection != null) {
            try {
                connection.close();
                log.info("Connection closed successfully.");
            } catch (SQLException e) {
                log.error("Error closing connection", e);
            }
        }
    }

    /**
     * Возвращает список всех пользователей из базы данных.
     * <p>
     * Пользователи извлекаются из таблицы {@code car_shop.user} и создаются объекты {@link User}.
     * </p>
     *
     * @return Список всех пользователей.
     */
    @Override
    public List<User> getAll() {
        List <User> users = new ArrayList<>();
        String query = "SELECT * FROM car_shop.user";
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
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

    /**
     * Фильтрует пользователей на основе заданного предиката.
     * <p>
     * Пользователи сначала извлекаются из базы данных, затем фильтруются с использованием предоставленного предиката.
     * </p>
     *
     * @param <T> Тип значения, на основе которого выполняется фильтрация.
     * @param getter Функция для извлечения значения из объекта {@link User}.
     * @param predicate Предикат для проверки значений.
     * @return Отфильтрованный список пользователей.
     */
    @Override
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        log.info("Get all users after filter");
        return getAll().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    /**
     * Сортирует пользователей на основе указанного ключа.
     * <p>
     * Пользователи сначала извлекаются из базы данных, затем сортируются с использованием предоставленного ключа.
     * </p>
     *
     * @param <T> Тип ключа для сортировки, который должен реализовывать {@link Comparable}.
     * @param keyExtractor Функция для извлечения ключа из объекта {@link User}.
     * @return Отсортированный список пользователей.
     */
    @Override
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        log.info("Get all users after sort");
        return getAll().stream()
                .sorted(Comparator.comparing(keyExtractor)).toList();
    }

    /**
     * Обновляет информацию о пользователе в базе данных.
     * <p>
     * Обновляются поля {@code login}, {@code password}, {@code name}, {@code age} и {@code city} для пользователя с указанным идентификатором.
     * </p>
     *
     * @param user Объект {@link User}, содержащий обновленную информацию.
     * @return Обновленный объект {@link User}.
     */
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
