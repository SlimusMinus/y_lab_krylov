package org.example;

import org.example.model.Car;
import org.example.model.Roles;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.CarService;
import org.example.service.UserInformation;
import org.example.service.jdbc.AuthServiceJdbc;
import org.example.service.jdbc.CarServiceJdbc;
import org.example.service.jdbc.UserInformationJdbc;

import java.util.Set;

import static org.example.out.OutData.startApp;

/**
 * @author Alexandr Krylov
 * Главный класс приложения, содержащий метод {@code main}.
 *
 * <p>Этот класс служит точкой входа в приложение и запускает метод {@link #startApp()},
 * который инициирует выполнение программы.</p>
 */
public class App {
    public static void main(String[] args) {

        startApp();

        /*CarService carService = new CarServiceJdbc();
        carService.getAll().forEach(System.out::println);*/

      /*  AuthService authService = new AuthServiceJdbc();
        User administrator = new User("wolf", "wolf", "Timur", 41, "Moscow", Set.of(Roles.ADMINISTRATOR, Roles.MANGER, Roles.CLIENT), null);
        authService.registeredUser(administrator);*/

       /* UserInformation carService = new UserInformationJdbc();
        carService.getAll().forEach(System.out::println);*/


    }
}



