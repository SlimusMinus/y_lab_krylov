package org.example.in;

import org.example.model.Car;
import org.example.model.Order;
import org.example.repository.UserData;
import org.example.service.OrderManagement;
import org.example.service.inMemory.OrderManagementImpl;

import java.time.LocalDate;
import java.util.Scanner;

import static org.example.in.InputData.checkInput;
/**
 * Класс для обработки данных заказов. Содержит методы для управления заказами.
 */
public class InputOrderData {

    private static Scanner scanner = new Scanner(System.in);
    /**
     * Метод для обработки информации о заказах. Позволяет создавать, просматривать, искать, изменять статус,
     * отменять заказы и выполнять фильтрацию.
     *
     * @param userId идентификатор пользователя, выполняющего действия
     */
    public static void ordersInformation(int userId) {
        int exit;
        do {
            OrderManagement orderManagement = new OrderManagementImpl();
            System.out.println("Выберите действие 1 - создать заказ, 2 - просмотр всех заказов 3 - поиск заказа " +
                    "4 - изменение статуса заказа, 5 - отмена заказа 6 - фильтрация");
            int choice = checkInput(6);
            switch (choice) {
                case 1: {
                    final Order newOrder = getNewOrder(userId);
                    orderManagement.create(newOrder);
                }
                break;
                case 2: {
                    orderManagement.getAll().forEach(System.out::println);
                }
                break;
                case 3: {
                    System.out.println("Введите порядковый номер заказа");
                    int idOrder = checkInput(orderManagement.getAll().size());
                    final Order orderById = orderManagement.getById(idOrder);
                    System.out.println(orderById);
                }
                break;
                case 4: {
                    orderManagement.getAll().forEach(System.out::println);
                    System.out.println("Введите порядковый номер заказа");
                    int idOrder = checkInput(orderManagement.getAll().size());
                    System.out.println("Введите новый статус");
                    String newStatus = scanner.next();
                    orderManagement.changeStatus(idOrder, newStatus);
                }
                break;
                case 5: {
                    orderManagement.getAll().forEach(System.out::println);
                    System.out.println("Введите порядковый номер заказа");
                    int idOrder = checkInput(orderManagement.getAll().size());
                    orderManagement.canceled(idOrder);
                }
                break;
                case 6: {
                    orderManagement.filter(Order::getDate, date -> date.isEqual(LocalDate.parse("2024-12-12"))).forEach(System.out::println);
                }
                break;
            }
            System.out.println("Если вы хотите продолжить работу с обработкой заказов нажмите 1, если нет - 2");
            exit = checkInput(2);
        } while (exit != 2);
    }
    /**
     * Создает и возвращает новый заказ на основе введенных данных.
     *
     * @param userId идентификатор пользователя, создающего заказ
     * @return новый заказ с введенными данными
     */
    private static Order getNewOrder(int userId) {
        Order newOrder = new Order();
        Car car = new Car();
        System.out.println("Введите брэнд");
        String brand = scanner.next();
        car.setBrand(brand);
        System.out.println("Введите модель");
        String model = scanner.next();
        car.setModel(model);
        System.out.println("Введите год");
        int year = checkInput(LocalDate.now().getYear());
        car.setYear(year);
        System.out.println("Введите цену");
        double price = scanner.nextDouble();
        car.setPrice(price);
        System.out.println("Введите состояние");
        String condition = scanner.next();
        car.setCondition(condition);
        newOrder.setCar(car);
        newOrder.setUser(UserData.getUsers().get(userId));
        newOrder.setDate(LocalDate.now());
        newOrder.setStatus("order created");
        return newOrder;
    }
}
