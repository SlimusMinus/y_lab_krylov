package org.example.model;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * Класс, представляющий пользователя в системе.
 *
 * <p>Класс содержит информацию о пользователе, такую как логин, пароль, имя, возраст, город, роль и список заказов.</p>
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
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
