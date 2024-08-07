package org.example.out;

import org.example.config.LiquibaseBaseConfig;
import org.example.in.InputData;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.inMemory.AuthServiceImpl;

import static org.example.in.InputCarData.carControl;
import static org.example.in.InputUserData.clientInformation;
import static org.example.in.InputData.checkInput;
import static org.example.in.InputOrderData.ordersInformation;

/**
 * Класс, содержащий методы для управления основным потоком приложения.
 *
 * <p>Этот класс предоставляет функциональность для регистрации пользователей, их аутентификации и выполнения различных действий
 * в зависимости от выбора пользователя. Действия включают управление автомобилями, обработку заказов и просмотр информации о клиентах
 * и сотрудниках.</p>
 */
public class OutData {

    private static final AuthService authService = new AuthServiceImpl();

    /**
     * Запускает основной поток приложения, предоставляя пользователю возможность выбрать действие.
     *
     * <p>Возможные действия включают регистрацию нового пользователя, вход в систему и выход из приложения.
     * Пользователь может выбрать одно из доступных действий через консольный интерфейс.</p>
     */
    public static void startApp() {
        LiquibaseBaseConfig.get();
        boolean exit = false;
        while (!exit) {
            System.out.println("Выберите действие: 1 - зарегистрироваться, 2 - войти, 3 - выйти");
            int choice = checkInput(3);
            switch (choice) {
                case 1:
                    registerNewUser();
                case 2:
                    userAction();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     *
     * <p>Запрашивает у пользователя данные для регистрации, создает новый объект {@link User} и регистрирует его с помощью
     * {@link AuthService}. После успешной регистрации выводит сообщение о том, что регистрация завершена.</p>
     */
    private static void registerNewUser() {
        final User newUser = InputData.getNewUser();
        authService.registeredUser(newUser);
        System.out.println(newUser + "\n вы успешно зарегистрированы");
    }

    /**
     * Обрабатывает действия аутентифицированного пользователя.
     *
     * <p>После успешной аутентификации пользователя предоставляет ему выбор действий, включая управление автомобилями,
     * обработку заказов и просмотр информации о клиентах и сотрудниках. В зависимости от выбранного действия вызываются
     * соответствующие методы.</p>
     */
    private static void userAction() {
        final Integer userId = InputData.authenticationUser(authService);
        if (userId != null) {
            System.out.println("Выберите действие 1 - Управление автомобилями 2 - Обработка заказов 3 - Просмотр информации о клиентах и сотрудниках");
            int choiceAction = checkInput(3);
            switch (choiceAction) {
                case 1 -> carControl();
                case 2 -> ordersInformation(userId);
                case 3 -> clientInformation();
            }
        }
    }
}
