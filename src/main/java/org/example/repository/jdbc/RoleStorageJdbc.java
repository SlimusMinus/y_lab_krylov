package org.example.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.example.config.DatabaseConfig;
import org.example.model.Roles;
import org.example.repository.RoleStorage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Реализация интерфейса {@link RoleStorage} для управления данными ролей пользователей в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для получения ролей пользователя по его идентификатору.
 * </p>
 */
@Slf4j
public class RoleStorageJdbc implements RoleStorage {

    private Connection connection;

    /**
     * Конструктор класса, устанавливающий соединение с базой данных.
     * <p>
     * В случае ошибки при подключении, выбрасывается исключение {@link RuntimeException}.
     * </p>
     */
    public RoleStorageJdbc() {
        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException | IOException e) {
            log.error("Error get connection", e);
        }
    }

    /**
     * Возвращает набор ролей пользователя по его идентификатору.
     * <p>
     * Роли извлекаются из таблицы {@code car_shop.user_roles} и конвертируются из строковых значений в перечисления {@link Roles}.
     * </p>
     *
     * @param id Идентификатор пользователя, для которого нужно получить роли.
     * @return Набор ролей {@link Roles}, ассоциированных с указанным пользователем.
     */
    @Override
    public Set<Roles> getById(int id) {
        Set<Roles> roles = new HashSet<>();
        String query = "SELECT role FROM car_shop.user_roles WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String roleName = resultSet.getString("role");
                    Roles role = mapRoleNameToEnum(roleName);
                    if (role != null) {
                        roles.add(role);
                    } else {
                        log.warn("Unknown role: " + roleName);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching roles for user with id " + id, e);
        }
        return roles;
    }

    /**
     * Преобразует строковое представление роли в соответствующее перечисление {@link Roles}.
     *
     * @param roleName Строковое представление роли.
     * @return Перечисление {@link Roles}, соответствующее переданной строке, или {@code null}, если строка не соответствует ни одной роли.
     */
    private Roles mapRoleNameToEnum(String roleName) {
        return switch (roleName.toLowerCase()) {
            case "администратор" -> Roles.ADMINISTRATOR;
            case "менеджер" -> Roles.MANAGER;
            case "клиент" -> Roles.CLIENT;
            default -> null;
        };
    }
}
