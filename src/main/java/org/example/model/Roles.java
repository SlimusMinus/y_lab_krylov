package org.example.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Перечисление, представляющее различные роли пользователей в системе.
 *
 * <p>Каждая роль имеет связанный с ней заголовок, который используется для удобного отображения роли.</p>
 *
 */
@Getter
@ToString
public enum Roles {
    ADMINISTRATOR("Администратор"),
    MANGER("Менеджер"),
    CLIENT("Клиент");

    private final String title;

    Roles(String title) {
        this.title = title;
    }

}
