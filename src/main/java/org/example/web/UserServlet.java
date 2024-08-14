package org.example.web;

import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;
import org.example.util.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Servlet для управления пользователями.
 * Обрабатывает запросы на отображение, фильтрацию, сортировку, обновление и создание пользователей.
 */
public class UserServlet extends HttpServlet {
    private UserStorage storage;

    /**
     * Инициализация сервлета.
     * Загружает драйвер PostgreSQL и инициализирует объект {@link UserStorage} для работы с хранилищем пользователей.
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
        storage = new UserStorageJdbc();
        super.init();
    }

    /**
     * Обрабатывает GET-запросы.
     * В зависимости от параметра "action" выполняет отображение списка пользователей, фильтрацию, сортировку или обновление пользователя.
     *
     * @param req  запрос, содержащий параметры для обработки.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException      если происходит ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        if (action == null) {
            showAll(req, resp);
        }
        switch (Objects.requireNonNull(action)) {
            case "filter":
                String nameFilter = req.getParameter("name-filter");
                String params = req.getParameter("params");
                req.setAttribute("cars", getFilteredUsers(nameFilter, params));
                req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
                break;
            case "sort":
                String paramsSort = req.getParameter("params-sort");
                req.setAttribute("cars", getSortedUsers(paramsSort));
                req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
                break;
            case "update":
                User user = storage.getById(Integer.parseInt(id));
                req.setAttribute("user", user);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-user.jsp").forward(req, resp);
                break;
            default:
                showAll(req, resp);
        }
    }

    /**
     * Обрабатывает POST-запросы.
     * Обновляет информацию о пользователе на основе переданных параметров.
     *
     * @param req  запрос, содержащий данные для обновления пользователя.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException      если происходит ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String city = req.getParameter("city");
        User user = new User(id, login, password, name, age, city, null, null);
        storage.update(user);
        resp.sendRedirect("users");
    }

    /**
     * Отображает список всех пользователей.
     *
     * @param req  запрос, содержащий данные для отображения.
     * @param resp ответ на запрос.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException      если происходит ошибка ввода-вывода при обработке запроса.
     */
    private void showAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = storage.getAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }

    /**
     * Возвращает список пользователей, отсортированный по указанному параметру.
     *
     * @param paramsSort параметр сортировки (например, "name", "age", "city").
     * @return список отсортированных пользователей.
     * @throws NotFoundException если параметр сортировки не соответствует ожидаемым значениям.
     */
    private List<User> getSortedUsers(String paramsSort) {
        return switch (paramsSort) {
            case "name" -> storage.sort(User::getName);
            case "age" -> storage.sort(User::getAge);
            case "city" -> storage.sort(User::getCity);
            default -> throw new NotFoundException("Unexpected value: " + paramsSort);
        };
    }

    /**
     * Возвращает список пользователей, отфильтрованных по указанным параметрам.
     *
     * @param nameFilter имя фильтра (например, "age", "name", "city").
     * @param params     значение фильтра.
     * @return список отфильтрованных пользователей.
     * @throws NotFoundException если фильтр не соответствует ожидаемым значениям.
     */
    private List<User> getFilteredUsers(String nameFilter, String params) {
        return switch (nameFilter) {
            case "name" -> storage.filter(User::getName, name -> name.equals(params));
            case "age" -> storage.filter(User::getAge, age -> age == Integer.parseInt(params));
            case "city" -> storage.filter(User::getCity, city -> city.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }
}
