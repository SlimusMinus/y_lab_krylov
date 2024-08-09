package org.example;

import org.example.repository.jdbc.OrderStorageJdbc;

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

            OrderStorageJdbc orderStorageJdbc = new OrderStorageJdbc();
            orderStorageJdbc.getAll().forEach(System.out::println);
        }
    }



