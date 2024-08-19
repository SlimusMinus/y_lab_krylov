package org.example.web;

import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.ObjectValidator;
import org.example.web.json.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet для управления пользователями.
 * Обрабатывает запросы на отображение, фильтрацию, сортировку, обновление и создание пользователей.
 */
public class UserServlet extends HttpServlet {
    private UserService service;
    private ObjectValidator objectValidator;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        service = new UserService();
        objectValidator = new ObjectValidator();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        if (action != null && action.equals("filter")) {
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            final List<User> filteredUsers = service.getFilteredUsers(nameFilter, params);
            showAll(resp, filteredUsers);
        } else if (action != null && action.equals("sort")) {
            String paramsSort = req.getParameter("params-sort");
            showAll(resp, service.getSortedUsers(paramsSort));
        } else {
            showAll(resp, service.getAll());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        UserDTO updateUserDTO = JsonUtil.readValue(req.getReader().lines().collect(Collectors.joining()), UserDTO.class);
        String id = req.getParameter("id");
        if (objectValidator.isValidObjectDTO(resp, updateUserDTO)) {
            User updateUser = UserMapper.INSTANCE.getUser(updateUserDTO);
            updateUser.setUserId(Integer.parseInt(id));
            service.update(updateUser);
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

}
