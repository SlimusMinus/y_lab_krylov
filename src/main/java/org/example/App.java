package org.example;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;

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
        //startApp();
        CarStorage storage = new CarStorageJdbc();
        final Car car = storage.getById(2);
        System.out.println(car);


    }
}



