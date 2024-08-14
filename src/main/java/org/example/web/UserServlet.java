package org.example.web;

import org.example.model.Car;
import org.example.model.User;
import org.example.repository.UserStorage;
import org.example.repository.jdbc.CarStorageJdbc;
import org.example.repository.jdbc.UserStorageJdbc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {
    private UserStorage storage;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if(action == null){
            showAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private void showAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = storage.getAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }
}
