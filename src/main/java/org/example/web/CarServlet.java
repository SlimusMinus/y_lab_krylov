package org.example.web;

import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;
import org.example.util.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Servlet для управления объектами {@link Car}.
 * Обрабатывает запросы на отображение, фильтрацию, добавление, редактирование и удаление автомобилей.
 */
public class CarServlet extends HttpServlet {

    private CarStorage storage;

    /**
     * Инициализация сервлета.
     * Загружает драйвер PostgreSQL и инициализирует объект {@link CarStorage} для работы с хранилищем автомобилей.
     *
     * @throws ServletException если происходит ошибка при инициализации сервлета.
     */
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = new CarStorageJdbc();
        super.init();
    }

    /**
     * Обрабатывает GET-запросы.
     * В зависимости от параметра "action" выполняет фильтрацию, удаление, редактирование автомобилей
     * или отображение списка всех автомобилей.
     *
     * @param req  запрос, содержащий параметры.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException если происходит ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            showAll(req, resp);
        }
        String id = req.getParameter("id");
        switch (Objects.requireNonNull(action)) {
            case "filter":
                String nameFilter = req.getParameter("name-filter");
                String params = req.getParameter("params");
                req.setAttribute("cars", getFilteredCars(nameFilter, params));
                req.getRequestDispatcher("/WEB-INF/jsp/cars.jsp").forward(req, resp);
                break;
            case "delete":
                storage.delete(Integer.parseInt(id));
                resp.sendRedirect("cars");
                break;
            case "edit":
                Car car;
                if (id == null) {
                    car = new Car();
                } else {
                    car = storage.getById(Integer.parseInt(id));
                }
                req.setAttribute("car", car);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-car.jsp").forward(req, resp);
                break;
            default:
                showAll(req, resp);
        }
    }

    /**
     * Обрабатывает POST-запросы.
     * Сохраняет или обновляет информацию об автомобиле на основе переданных параметров.
     *
     * @param req  запрос, содержащий данные автомобиля.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException если происходит ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String brand = req.getParameter("brand");
        String model = req.getParameter("model");
        int year = Integer.parseInt(req.getParameter("year"));
        double price = Double.parseDouble(req.getParameter("price"));
        String condition = req.getParameter("condition");
        Car newCar;
        if (id == null || id.isEmpty()) {
            newCar = new Car(brand, model, year, price, condition);
        } else {
            newCar = new Car(Integer.parseInt(id), brand, model, year, price, condition);
        }
        storage.saveOrUpdate(newCar);
        resp.sendRedirect("cars");
    }

    /**
     * Возвращает список автомобилей, отфильтрованный по указанным параметрам.
     *
     * @param nameFilter имя фильтра (например, "brand", "condition", "price").
     * @param params     значение фильтра.
     * @return список отфильтрованных автомобилей.
     * @throws NotFoundException если фильтр не соответствует ожидаемым значениям.
     */
    private List<Car> getFilteredCars(String nameFilter, String params) {
        return switch (nameFilter) {
            case "brand" -> storage.filter(Car::getBrand, brand -> brand.equals(params));
            case "condition" -> storage.filter(Car::getCondition, condition -> condition.equals(params));
            case "price" -> storage.filter(Car::getPrice, price -> price == (Integer.parseInt(params)));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

    /**
     * Отображает список всех автомобилей.
     *
     * @param req  запрос, содержащий данные для отображения.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException если происходит ошибка ввода-вывода при обработке запроса.
     */
    private void showAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Car> cars = storage.getAll();
        req.setAttribute("cars", cars);
        req.getRequestDispatcher("/WEB-INF/jsp/cars.jsp").forward(req, resp);
    }
}
