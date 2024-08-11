package org.example.repository;

import org.example.model.User;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Интерфейс для управления информацией о пользователях.
 * <p>
 * Этот интерфейс определяет методы для получения, фильтрации, сортировки и редактирования пользователей.
 * </p>
 */
public interface UserStorage {

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список объектов {@link User}, представляющих всех пользователей.
     */
    List<User> getAll();

    /**
     * Фильтрует пользователей по заданному критерию.
     *
     * @param <T>       Тип возвращаемого значения функции {@code getter}.
     * @param getter    Функция для получения значения, по которому будет производиться фильтрация.
     * @param predicate Условие для фильтрации пользователей.
     * @return Список пользователей, удовлетворяющих условию фильтрации.
     */
    <T> List<User> filter(Function<User, T> getter, Predicate <T> predicate);

    /**
     * Сортирует пользователей по заданному критерию.
     *
     * @param <T>          Тип значения, по которому будет производиться сортировка.
     * @param keyExtractor Функция для получения значения, по которому будет производиться сортировка.
     * @return Отсортированный список пользователей.
     */
    <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor);

    /**
     * Редактирует информацию о пользователе.
     *
     * @param user Объект {@link User}, представляющий пользователя с обновленной информацией.
     * @return Объект {@link User} с обновленной информацией.
     */
    User update(User user);
}
