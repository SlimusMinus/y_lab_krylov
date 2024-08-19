package org.example.service;

import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;
import org.example.util.NotFoundException;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserService {
    private final UserStorage storage;

    public UserService() {
        storage = new UserStorageJdbc();
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getById(int id) {
        return storage.getById(id);
    }

    public <T> List<User> filter(Function<User, T> getter, Predicate<T> predicate) {
        return storage.filter(getter, predicate);
    }

    public <T extends Comparable<T>> List<User> sort(Function<User, T> keyExtractor) {
        return storage.sort(keyExtractor);
    }

    public User update(User user) {
        return storage.update(user);
    }


    public List<User> getSortedUsers(String paramsSort) {
        return switch (paramsSort) {
            case "name" -> sort(User::getName);
            case "age" -> sort(User::getAge);
            case "city" -> sort(User::getCity);
            default -> throw new NotFoundException("Unexpected value: " + paramsSort);
        };
    }

    public List<User> getFilteredUsers(String nameFilter, String params) {
        return switch (nameFilter) {
            case "name" -> filter(User::getName, name -> name.equals(params));
            case "age" -> filter(User::getAge, age -> age == Integer.parseInt(params));
            case "city" -> filter(User::getCity, city -> city.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }
}
