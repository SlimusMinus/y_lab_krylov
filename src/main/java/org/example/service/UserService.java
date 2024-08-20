package org.example.service;

import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;
import org.example.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getById(int id) {
        return storage.getById(id);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public List<User> getSortedUsers(String paramsSort) {
        return switch (paramsSort) {
            case "name" -> storage.sort(User::getName);
            case "age" -> storage.sort(User::getAge);
            case "city" -> storage.sort(User::getCity);
            default -> throw new NotFoundException("Unexpected value: " + paramsSort);
        };
    }

    public List<User> getFilteredUsers(String nameFilter, String params) {
        return switch (nameFilter) {
            case "name" -> storage.filter(User::getName, name -> name.equals(params));
            case "age" -> storage.filter(User::getAge, age -> age == Integer.parseInt(params));
            case "city" -> storage.filter(User::getCity, city -> city.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }
}
