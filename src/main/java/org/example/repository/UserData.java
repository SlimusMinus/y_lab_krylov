package org.example.repository;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Order;
import org.example.model.Roles;
import org.example.model.User;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс, содержащий статические данные о пользователях.
 *
 * <p>Этот класс предоставляет статические данные о различных пользователях системы, включая их идентификаторы, логины, пароли,
 * имена, возраст, города, роли и связанные заказы. Он инициализирует коллекцию {@link Map} для хранения информации о каждом пользователе,
 * где ключом является уникальный идентификатор пользователя, а значением - объект {@link User}.</p>
 */
public class UserData {
    /**
     * Текущий идентификатор пользователя, используемый для создания новых пользователей.
     *
     * <p>Этот идентификатор автоматически увеличивается при создании нового пользователя.</p>
     */
    @Getter
    private static int UserId = 1;
    private static final List<Order> oder1 = Arrays.asList(new Order(1, LocalDate.parse("2024-12-12"), CarData.getCars().get(1), "successfully"));
    private static final List<Order> oder2 = Arrays.asList(new Order(2, LocalDate.parse("2024-11-11"), CarData.getCars().get(2), "successfully"));
    private static final User administrator = new User(UserId++, "admin", "admin", "Alexandr", 33, "Moscow", Set.of(Roles.ADMINISTRATOR), null);
    private static final User manager1 = new User(UserId++, "manager1", "manager1", "John", 36, "New-York",  Set.of(Roles.MANGER), null);
    private static final User manager2 = new User(UserId++, "manager2", "manager2", "Alexandr", 34, "Moscow",  Set.of(Roles.MANGER), oder1);
    private static final User client1 = new User(UserId++, "client1", "client1", "Tanya", 25, "London",  Set.of(Roles.CLIENT), oder2);
    private static final User client2 = new User(UserId++, "client2", "client2", "Valera", 45, "Milan",  Set.of(Roles.CLIENT), null);
    private static final User client3 = new User(UserId++, "client3", "client3", "Robert", 33, "Moscow",  Set.of(Roles.CLIENT), null);
    /**
     * Коллекция для хранения информации о всех пользователях.
     *
     * <p>Ключом является уникальный идентификатор пользователя, а значением - объект {@link User}.</p>
     */
    @Getter
    @Setter
    private static Map<Integer, User> users = new HashMap<>();

    static {
        users.put(administrator.getId(), administrator);
        users.put(manager1.getId(), manager1);
        users.put(manager2.getId(), manager2);
        users.put(client1.getId(), client1);
        users.put(client2.getId(), client2);
        users.put(client3.getId(), client3);
    }
}