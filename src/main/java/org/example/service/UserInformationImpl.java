package org.example.service;

import org.example.model.User;
import org.example.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link UserInformation} для управления информацией о пользователях.
 * <p>
 * Этот класс предоставляет методы для получения, фильтрации, сортировки и редактирования пользователей.
 * Он использует данные пользователей из {@link UserData} и логирует действия через SLF4J.
 * </p>
 */
public class UserInformationImpl implements UserInformation {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final List<User> list = UserData.getUsers().values().stream().toList();

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список объектов {@link User}, представляющих всех пользователей.
     */
    @Override
    public List<User> getAll() {
        log.info("Get all users");
        return UserData.getUsers().values().stream().toList();
    }

    /**
     * Фильтрует пользователей по заданному критерию.
     *
     * @param <T>       Тип возвращаемого значения функции {@code getter}.
     * @param getter    Функция для получения значения, по которому будет производиться фильтрация.
     * @param predicate Условие для фильтрации пользователей.
     * @return Список пользователей, удовлетворяющих условию фильтрации.
     */
    @Override
    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        log.info("Get all users after filter");
        return list.stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    /**
     * Сортирует пользователей по заданному критерию.
     *
     * @param <T>          Тип значения, по которому будет производиться сортировка.
     * @param keyExtractor Функция для получения значения, по которому будет производиться сортировка.
     * @return Отсортированный список пользователей.
     */
    @Override
    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        log.info("Get all users after sort");
        return list.stream()
                .sorted(Comparator.comparing(keyExtractor)).toList();
    }

    /**
     * Редактирует информацию о пользователе.
     *
     * @param user Объект {@link User}, представляющий пользователя с обновленной информацией.
     * @return Объект {@link User} с обновленной информацией.
     */
    @Override
    public User edit(User user) {
        log.info("User {} user was changed", user);
        return UserData.getUsers().put(user.getId(), user);
    }
}
