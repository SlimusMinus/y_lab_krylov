package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Roles;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализация интерфейса {@link RoleStorage} для управления данными ролей пользователей в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для получения ролей пользователя по его идентификатору.
 * </p>
 */
@Repository
@Slf4j
public class RoleStorage {

    private static final BeanPropertyRowMapper<Roles> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Roles.class);
    private final JdbcTemplate jdbcTemplate;

    public RoleStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получает набор ролей для пользователя по его идентификатору.
     *
     * <p>
     * Метод выполняет запрос к базе данных для получения списка ролей пользователя, используя идентификатор пользователя.
     * Роли преобразуются в перечисление {@code Roles} и возвращаются в виде {@code Set}. Если роль не распознана, она будет пропущена.
     * </p>
     *
     * @param id Идентификатор пользователя, для которого требуется получить роли.
     * @return Набор ролей {@code Roles} для указанного пользователя. Если пользователь не имеет ролей, возвращается пустой набор.
     * @throws DataAccessException если возникает ошибка доступа к данным во время выполнения запроса.
     */
    public Set<Roles> getById(int id) {
        List<Roles> listRoles = jdbcTemplate.query( "SELECT role FROM car_shop.user_roles WHERE user_id = ?", ROW_MAPPER, id);
        Set <Roles> roles = new HashSet<>();
        for(Roles role : listRoles){
            roles.add(mapRoleNameToEnum(role.name()));
        }
        return new HashSet<>(roles);
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
