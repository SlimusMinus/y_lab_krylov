package org.example.in;

import org.example.model.Roles;
import org.example.model.User;
import org.example.service.AuthService;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

/**
 * Класс для работы с входными данными пользователя.
 * Содержит методы для регистрации, аутентификации и проверки ввода.
 */
public class InputAuthData {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Создает и возвращает нового пользователя на основе введенных данных.
     *
     * @return новый пользователь с введенными данными
     */
    public static User getNewUser() {
        User newUser = new User();
        System.out.println("Добро пожаловать в окно регистрации");
        System.out.println("Введите логин");
        String login = scanner.next();
        newUser.setLogin(login);
        System.out.println("Введите пароль");
        String password = scanner.next();
        newUser.setPassword(password);
        System.out.println("Введите имя");
        String name = scanner.next();
        newUser.setName(name);
        System.out.println("Введите возраст");
        int age = checkInput(150);
        newUser.setAge(age);
        System.out.println("Введите город");
        String city = scanner.next();
        newUser.setCity(city);
        newUser.setRole(Set.of(Roles.CLIENT));
        return newUser;
    }
    /**
     * Выполняет аутентификацию пользователя по введенному логину и паролю.
     *
     * @param authService сервис для аутентификации пользователей
     * @return идентификатор пользователя, если аутентификация успешна, иначе null
     */
    public static Integer authenticationUser(AuthService authService) {
        System.out.println("Введите логин ");
        String login = scanner.next();
        System.out.println("Введите пароль ");
        String password = scanner.next();
        if (authService.loginUser(login, password) != 0) {
            System.out.println("Вы успешно вошли");
            return authService.loginUser(login, password);
        } else {
            System.out.println("Неправильное имя пользователя или пароль");
            return null;
        }
    }
    /**
     * Проверяет корректность ввода числа и возвращает введенное число, если оно находится в заданном диапазоне.
     *
     * @param maxNumber максимальное допустимое число
     * @return введенное число, если оно корректно
     */
    public static int checkInput(int maxNumber){
        int choice = -1;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= maxNumber) {
                    break;
                } else {
                    System.out.println("Введите число от 1 до " + maxNumber + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число.");
                scanner.nextLine();
            }
        }
        return choice;
    }
}
