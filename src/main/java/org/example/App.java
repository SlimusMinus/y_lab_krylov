package org.example;

import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;

import java.util.List;

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
    }
}



