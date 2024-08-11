package org.example.model;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * Представляет сущность пользователя в системе, содержащую информацию о пользователе,
 * его ролях и заказах.
 * <p>
 * Класс включает поля:
 * <ul>
 *   <li><b>userId</b> - уникальный идентификатор пользователя.</li>
 *   <li><b>login</b> - логин пользователя для авторизации в системе.</li>
 *   <li><b>password</b> - пароль пользователя для авторизации в системе.</li>
 *   <li><b>name</b> - имя пользователя.</li>
 *   <li><b>age</b> - возраст пользователя.</li>
 *   <li><b>city</b> - город проживания пользователя.</li>
 *   <li><b>role</b> - набор ролей пользователя в системе (например, администратор, менеджер).</li>
 *   <li><b>orders</b> - список заказов, связанных с пользователем.</li>
 * </ul>
 * </p>
 * <p>
 * Класс предоставляет два конструктора:
 * <ul>
 *   <li>Конструктор с параметрами для всех полей, включая идентификатор пользователя.</li>
 *   <li>Конструктор без параметра идентификатора пользователя, используемый при создании нового пользователя.</li>
 * </ul>
 * </p>
 * <p>
 * Класс также включает аннотации от библиотеки Lombok:
 * <ul>
 *   <li>@Data - Генерирует методы getter, setter, toString, equals и hashCode.</li>
 *   <li>@AllArgsConstructor - Генерирует конструктор с параметрами для всех полей.</li>
 *   <li>@NoArgsConstructor - Генерирует конструктор без параметров.</li>
 * </ul>
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String login;
    private String password;
    private String name;
    private int age;
    private String city;
    private Set <Roles> role;
    private List<Order> orders;

    public User(String login, String password, String name, int age, String city, Set <Roles> role, List<Order> orders) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.age = age;
        this.city = city;
        this.role = role;
        this.orders = orders;
    }
}
