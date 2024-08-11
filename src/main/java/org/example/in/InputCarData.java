package org.example.in;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.inMemory.CarStorageInMemory;

import java.time.LocalDate;
import java.util.Scanner;

import static org.example.in.InputAuthData.checkInput;

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
            CarStorage carStorage = new CarStorageInMemory();
            System.out.println("Выберите действие которое хотите совершить 1-показать все 2 - добавить новый" +
                    " 3 - редактирование авто 4 - удаление авто 5 - фильтрация");
            int choiceAuto = checkInput(4);
            switch (choiceAuto) {
                case 1:
                    carStorage.getAll().forEach(System.out::println);
                    break;
                case 2: {
                    Car newCar = new Car();
                    add(newCar, carStorage);
                }
                break;
                case 3: {
                    edit(carStorage);
                }
                break;
                case 4: {
                    delete(carStorage);
                }
                case 5: {
                    System.out.println("Выберите параметр фильтрации 1 - бренд, 2 - состояние, 3 - возраст");
                    int choiceFilter = checkInput(3);
                    filter(choiceFilter, carStorage);
                }
            }
            System.out.println("Если вы хотите продолжить работу с авто нажмите 1, если нет - 2");
            exit = checkInput(2);
        } while (exit != 2);
    }

    private static void add(Car newCar, CarStorage carStorage) {
        final Car car = saveOrUpdateCar(newCar);
        carStorage.saveOrUpdate(car);
    }

    private static void delete(CarStorage carStorage) {
        carStorage.getAll().forEach(System.out::println);
        System.out.println("Введите порядковый номер авто который хотите удалить");
        int number = checkInput(carStorage.getAll().size() + 1);
        carStorage.delete(number - 1);
    }

    private static void edit(CarStorage carStorage) {
        carStorage.getAll().forEach(System.out::println);
        System.out.println("Введите порядковый номер авто который хотите отредактировать");
        int number = checkInput(carStorage.getAll().size() + 1);
        Car carEdit = carStorage.getAll().get(number - 1);
        final Car car = saveOrUpdateCar(carEdit);
        carStorage.saveOrUpdate(car);
    }

    /**
     * Фильтрует автомобили по выбранному пользователем критерию.
     *
     * @param choiceFilter выбор критерия фильтрации: 1 - бренд, 2 - состояние, 3 - возраст
     * @param carStorage сервис для работы с автомобилями
     */
    private static void filter(int choiceFilter, CarStorage carStorage) {
        switch (choiceFilter) {
            case 1:
                carStorage.filter(Car::getBrand, brand -> brand.equals("Volvo"));
                break;
            case 2:
                carStorage.filter(Car::getCondition, condition -> condition.equals("new"));
                break;
            case 3:
                carStorage.filter(Car::getPrice, price -> price == 25000);
                break;
        }
    }
}
