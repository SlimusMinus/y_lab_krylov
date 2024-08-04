package org.example.in;

import org.example.model.Car;
import org.example.service.CarService;
import org.example.service.CarServiceImpl;

import java.time.LocalDate;
import java.util.Scanner;

import static org.example.in.InputData.checkInput;

/**
 * Класс для работы с данными автомобилей.
 * Содержит методы для создания, обновления, удаления и фильтрации автомобилей.
 */
public class InputCarData {
    private static Scanner scanner = new Scanner(System.in);
    /**
     * Сохраняет или обновляет информацию об автомобиле.
     *
     * @param car автомобиль, информацию о котором нужно сохранить или обновить
     * @return обновленный автомобиль
     */
    public static Car saveOrUpdateCar(Car car) {
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
        return car;
    }
    /**
     * Основной метод для управления автомобилями.
     * Позволяет пользователю выбирать действия: просмотр всех автомобилей, добавление нового,
     * редактирование, удаление и фильтрация автомобилей.
     */
    public static void carControl() {
        int exit;
        do {
            CarService carService = new CarServiceImpl();
            System.out.println("Выберите действие которое хотите совершить 1-показать все 2 - добавить новый" +
                    " 3 - редактирование авто 4 - удаление авто 5 - фильтрация");
            int choiceAuto = checkInput(4);
            switch (choiceAuto) {
                case 1:
                    carService.getAll().forEach(System.out::println);
                    break;
                case 2: {
                    Car newCar = new Car();
                    add(newCar, carService);
                }
                break;
                case 3: {
                    edit(carService);
                }
                break;
                case 4: {
                    delete(carService);
                }
                case 5: {
                    System.out.println("Выберите параметр фильтрации 1 - бренд, 2 - состояние, 3 - возраст");
                    int choiceFilter = checkInput(3);
                    filter(choiceFilter, carService);
                }
            }
            System.out.println("Если вы хотите продолжить работу с авто нажмите 1, если нет - 2");
            exit = checkInput(2);
        } while (exit != 2);
    }

    private static void add(Car newCar, CarService carService) {
        final Car car = saveOrUpdateCar(newCar);
        carService.saveOrUpdate(car);
    }

    private static void delete(CarService carService) {
        carService.getAll().forEach(System.out::println);
        System.out.println("Введите порядковый номер авто который хотите удалить");
        int number = checkInput(carService.getAll().size() + 1);
        carService.delete(number - 1);
    }

    private static void edit(CarService carService) {
        carService.getAll().forEach(System.out::println);
        System.out.println("Введите порядковый номер авто который хотите отредактировать");
        int number = checkInput(carService.getAll().size() + 1);
        Car carEdit = carService.getAll().get(number - 1);
        final Car car = saveOrUpdateCar(carEdit);
        carService.saveOrUpdate(car);
    }

    /**
     * Фильтрует автомобили по выбранному пользователем критерию.
     *
     * @param choiceFilter выбор критерия фильтрации: 1 - бренд, 2 - состояние, 3 - возраст
     * @param carService сервис для работы с автомобилями
     */
    private static void filter(int choiceFilter, CarService carService) {
        switch (choiceFilter) {
            case 1:
                carService.filter(Car::getBrand, brand -> brand.equals("Volvo"));
                break;
            case 2:
                carService.filter(Car::getCondition, condition -> condition.equals("new"));
                break;
            case 3:
                carService.filter(Car::getPrice, price -> price == 25000);
                break;
        }
    }
}
