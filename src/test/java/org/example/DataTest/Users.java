package org.example.DataTest;

import lombok.Getter;
import org.example.model.Order;
import org.example.model.Roles;
import org.example.model.User;
import org.example.repository.CarData;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {
    @Getter
    private static int UserId = 1;
    public static final List<Order> oder1 = Arrays.asList(new Order(1, LocalDate.parse("2024-12-12"), CarData.getCars().get(1), "successfully"));
    public static final List<Order> oder2 = Arrays.asList(new Order(2, LocalDate.parse("2024-11-11"), CarData.getCars().get(2), "successfully"));
    public static final User administrator = new User(UserId++, "admin", "admin", "Alexandr", 33, "Moscow", Roles.ADMINISTRATOR, null);
    public static final User manager1 = new User(UserId++, "manager1", "manager1", "John", 36, "New-York", Roles.MANGER, null);
    public static final User manager2 = new User(UserId++, "manager2", "manager2", "Alexandr", 34, "Moscow", Roles.MANGER, oder1);
    public static final User client1 = new User(UserId++, "client1", "client1", "Tanya", 25, "London", Roles.CLIENT, oder2);
    public static final User client2 = new User(UserId++, "client2", "client2", "Valera", 45, "Milan", Roles.CLIENT, null);
    private static final User client3 = new User(UserId++, "client3", "client3", "Robert", 33, "Moscow", Roles.CLIENT, null);
    public static final User newAdministrator = new User("master", "master", "Tom", 78, "Tokyo", Roles.ADMINISTRATOR, null);
    public static final User editClient2 = new User(client2.getId(), "client222", "client222", "Pavel", 45, "Ivanovo", Roles.CLIENT, null);

    public static final List<User> userList = List.of(administrator, manager1, manager2, client1, client2, client3);
    public static final List<User> namePhilter = List.of(administrator, manager2);
    public static final List<User> cityPhilter = List.of(administrator, manager2, client3);
    public static final List<User> agePhilter = List.of(administrator, client3);
    public static final List<User> usersNameSort = List.of(administrator, manager2, manager1, client3, client1, client2);
    public static final List<User> usersAgeSort = List.of(client1, administrator, client3, manager2, manager1, client2);
    public static Map<Integer, User> testUsers = new HashMap<>();

    static {
        testUsers.put(administrator.getId(), administrator);
        testUsers.put(manager1.getId(), manager1);
        testUsers.put(manager2.getId(), manager2);
        testUsers.put(client1.getId(), client1);
        testUsers.put(client2.getId(), client2);
        testUsers.put(client3.getId(), client3);
    }

}
