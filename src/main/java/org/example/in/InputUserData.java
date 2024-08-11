package org.example.in;

import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.inMemory.UserStorageInMemory;

import java.util.Scanner;

import static org.example.in.InputAuthData.checkInput;

/**
 * Класс для обработки данных пользователей. Содержит методы для просмотра, фильтрации, сортировки и редактирования пользователей.
 */
public class InputUserData {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Метод для обработки информации о клиентах и сотрудниках. Позволяет просматривать, фильтровать, сортировать и редактировать пользователей.
     */
    public static void clientInformation() {
        int exit;
        do {
            System.out.println("Выберите действие 1 - просмотр всех пользователей, 2 - фильтрация 3 - сортировка 4 - редактирование");
            int choice = checkInput(4);
            UserStorage information = new UserStorageInMemory();
            switch (choice) {
                case 1: {
                    information.getAll().forEach(System.out::println);
                }
                break;
                case 2: {
                    System.out.println("Выберите параметр фильтрации 1 - город, 2 - имя, 3 - возраст");
                    int choiceFilter = checkInput(3);
                    filter(choiceFilter, information);
                }
                break;
                case 3: {
                    System.out.println("Выберите параметр сортировки 1 - город, 2 - имя, 3 - возраст");
                    int choiceSorted = checkInput(3);
                    sort(choiceSorted, information);
                }
                break;
                case 4: {
                    final User editUser = editUser(information);
                    information.update(editUser);
                }
                break;
            }System.out.println("Если вы хотите продолжить работу с просмотром информации о клиентах и сотрудниках нажмите 1, если нет - 2");
            exit = checkInput(2);
        } while (exit != 2);
    }

    /**
     * Метод для сортировки пользователей по заданному параметру.
     *
     * @param choiceSorted выбор параметра сортировки (1 - город, 2 - имя, 3 - возраст)
     * @param information объект UserInformation для выполнения операций
     */
    private static void sort(int choiceSorted, UserStorage information) {
        switch (choiceSorted) {
            case 1:
                information.sort(User::getCity).forEach(System.out::println);
                break;
            case 2:
                information.sort(User::getName).forEach(System.out::println);
                break;
            case 3:
                information.sort(User::getAge).forEach(System.out::println);
                break;
        }
    }

    /**
     * Метод для фильтрации пользователей по заданному параметру.
     *
     * @param choiceFilter выбор параметра фильтрации (1 - город, 2 - имя, 3 - возраст)
     * @param information объект UserInformation для выполнения операций
     */
    private static void filter(int choiceFilter, UserStorage information) {
        switch (choiceFilter) {
            case 1:
                information.filter(User::getCity, city -> city.equals("Moscow")).forEach(System.out::println);
                break;
            case 2:
                information.filter(User::getName, name -> name.equals("Alexandr")).forEach(System.out::println);
                break;
            case 3:
                information.filter(User::getAge, age -> age == 33).forEach(System.out::println);
                break;
        }
    }

    /**
     * Метод для редактирования данных пользователя.
     *
     * @param information объект UserInformation для выполнения операций
     * @return отредактированный пользователь
     */
    private static User editUser(UserStorage information) {
        information.getAll().forEach(System.out::println);
        System.out.println("Введите порядковый номер юзера");
        int choiceEdit = checkInput(information.getAll().size());
        User editUser = information.getAll().get(choiceEdit-1);
        System.out.println("Введите логин");
        String login = scanner.next();
        editUser.setLogin(login);
        System.out.println("Введите пароль");
        String password = scanner.next();
        editUser.setPassword(password);
        System.out.println("Введите имя");
        String name = scanner.next();
        editUser.setName(name);
        System.out.println("Введите возраст");
        int age = checkInput(150);
        editUser.setAge(age);
        System.out.println("Введите город");
        String city = scanner.next();
        editUser.setCity(city);
        return editUser;
    }

}
