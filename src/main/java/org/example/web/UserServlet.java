package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.UserStorageJdbc;
import org.example.util.NotFoundException;
import org.example.util.ValidationDTO;
import org.example.web.json.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Servlet для управления пользователями.
 * Обрабатывает запросы на отображение, фильтрацию, сортировку, обновление и создание пользователей.
 */
@Slf4j
public class UserServlet extends HttpServlet {
    private UserStorage storage;
    private ValidationDTO validationDTO;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = new UserStorageJdbc();
        validationDTO = new ValidationDTO();
        super.init();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        if (action != null && action.equals("filter")){
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            final List<User> filteredUsers = getFilteredUsers(nameFilter, params);
            showAll(resp, filteredUsers);
        } else if (action != null && action.equals("sort")) {
            String paramsSort = req.getParameter("params-sort");
            showAll(resp, getSortedUsers(paramsSort));
        } else {
            showAll(resp, storage.getAll());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        UserDTO updateUserDTO = JsonUtil.readValue(req.getReader().lines().collect(Collectors.joining()), UserDTO.class);
        String id = req.getParameter("id");
        if(validationDTO.isValidObjectDTO(resp, updateUserDTO)){
            User updateUser = UserMapper.INSTANCE.getUser(updateUserDTO);
            updateUser.setUserId(Integer.parseInt(id));
            storage.update(updateUser);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.sendRedirect("users");
        }
    }

    private void showAll(HttpServletResponse resp, List<User> users) throws ServletException, IOException {
        List<UserDTO> usersDTO = users.stream()
                .map(UserMapper.INSTANCE::getUserDTO)
                .toList();
        String jsonResponse = JsonUtil.writeValue(usersDTO);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse);
    }

    private List<User> getSortedUsers(String paramsSort) {
        return switch (paramsSort) {
            case "name" -> storage.sort(User::getName);
            case "age" -> storage.sort(User::getAge);
            case "city" -> storage.sort(User::getCity);
            default -> throw new NotFoundException("Unexpected value: " + paramsSort);
        };
    }

    private List<User> getFilteredUsers(String nameFilter, String params) {
        return switch (nameFilter) {
            case "name" -> storage.filter(User::getName, name -> name.equals(params));
            case "age" -> storage.filter(User::getAge, age -> age == Integer.parseInt(params));
            case "city" -> storage.filter(User::getCity, city -> city.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }
}
