package org.example.model;

import lombok.ToString;

/**
 * Перечисление, представляющее различные роли пользователей в системе.
 *
 * <p>Каждая роль имеет связанный с ней заголовок, который используется для удобного отображения роли.</p>
 *
 */
@ToString
public enum Roles {
    ADMINISTRATOR("Администратор"),
    MANGER("Менеджер"),
    CLIENT("Клиент");

    private String title;

    Roles(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title ;
    }
}
