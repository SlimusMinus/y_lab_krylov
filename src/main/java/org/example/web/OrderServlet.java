package org.example.web;

import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.repository.jdbc.OrderStorageJdbc;
import org.example.util.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrderServlet extends HttpServlet {
    private OrderStorage storage;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = new OrderStorageJdbc();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            showAll(req, resp);
        }
        String id = req.getParameter("id");
        switch (Objects.requireNonNull(action)) {
            case "get-by-id":
                List<Order> orderById = List.of(storage.getById(Integer.parseInt(id)));
                req.setAttribute("orders", orderById);
                req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
                break;
            case "filter":
                String nameFilter = req.getParameter("name-filter");
                String params = req.getParameter("params");
                req.setAttribute("cars", getFilteredOrder(nameFilter, params));
                req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
                break;
            case "edit":
                Order order = new Order();
                req.setAttribute("order", order);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-orders.jsp").forward(req, resp);
                break;
            case "canceled":
                storage.canceled(Integer.parseInt(id));
                showAll(req, resp);
                break;
            case "change status":
                String newStatus = req.getParameter("status");
                storage.changeStatus(Integer.parseInt(id), newStatus);
                showAll(req, resp);
                break;
            default:
                showAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int userId = Integer.parseInt(req.getParameter("userId"));
        int carId = Integer.parseInt(req.getParameter("carId"));
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        String status = req.getParameter("status");
        Order order = new Order(userId, carId, date, status);
        storage.create(order);
        resp.sendRedirect("orders");
    }

    private List<Order> getFilteredOrder(String nameFilter, String params) {
        return switch (nameFilter) {
            case "brand" -> storage.filter(Order::getDate, date -> date.isEqual(LocalDate.parse(params)));
            case "status" -> storage.filter(Order::getStatus, status -> status.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

    private void showAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orders = storage.getAll();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
    }
}
