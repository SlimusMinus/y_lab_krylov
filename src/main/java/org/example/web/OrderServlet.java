package org.example.web;

import org.example.dto.OrderDTO;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.service.OrderService;
import org.example.util.ObjectValidator;
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
 * Servlet для управления заказами.
 * Обрабатывает запросы на отображение, фильтрацию, добавление, редактирование, отмену и изменение статуса заказов.
 */
public class OrderServlet extends HttpServlet {
    private OrderService service;
    private ObjectValidator objectValidator;


    /**
     * Инициализация сервлета.
     * Загружает драйвер PostgreSQL и инициализирует объект {@link OrderStorage} для работы с хранилищем заказов.
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
        service = new OrderService();
        objectValidator = new ObjectValidator();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        if(action != null && action.equals("get-by-id")){
            Order orderById = service.getById(Integer.parseInt(id));
            String jsonResponse = JsonUtil.writeValue(OrderMapper.INSTANCE.getOdderDTO(orderById));
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);
        } else if (action != null && action.equals("filter")) {
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            final List<Order> filteredOrder = service.getFilteredOrder(nameFilter, params);
            showAll(resp, filteredOrder);
        } else {
            showAll(resp, service.getAll());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        switch (Objects.requireNonNull(action)) {
            case "canceled":
                service.canceled(Integer.parseInt(id));
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                showAll(resp, service.getAll());
                resp.sendRedirect("orders");
                break;
            case "change-status":
                String newStatus = req.getParameter("status");
                service.changeStatus(Integer.parseInt(id), newStatus);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                showAll(resp, service.getAll());
                break;
            default:
                showAll(resp, service.getAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        OrderDTO orderDTO = JsonUtil.readValue(req.getReader().lines().collect(Collectors.joining()), OrderDTO.class);
        if(objectValidator.isValidObjectDTO(resp, orderDTO)){
            Order order = OrderMapper.INSTANCE.getOrder(orderDTO);
            service.create(order);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.sendRedirect("orders");
        }
    }

    private void showAll(HttpServletResponse resp, List<Order> orders) throws ServletException, IOException {
        List<OrderDTO> ordersDTO = orders.stream()
                .map(OrderMapper.INSTANCE::getOdderDTO)
                .toList();
        String jsonResponse = JsonUtil.writeValue(ordersDTO);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse);
    }
}
