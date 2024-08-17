package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderDTO;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.repository.jdbc.OrderStorageJdbc;
import org.example.util.NotFoundException;
import org.example.util.ValidationDTO;
import org.example.web.json.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Servlet для управления заказами.
 * Обрабатывает запросы на отображение, фильтрацию, добавление, редактирование, отмену и изменение статуса заказов.
 */
@Slf4j
public class OrderServlet extends HttpServlet {
    private OrderStorage storage;
    private ValidationDTO validationDTO;


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
        storage = new OrderStorageJdbc();
        validationDTO = new ValidationDTO();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        if(action != null && action.equals("get-by-id")){
            Order orderById = storage.getById(Integer.parseInt(id));
            String jsonResponse = JsonUtil.writeValue(OrderMapper.INSTANCE.getOdderDTO(orderById));
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);
        } else if (action != null && action.equals("filter")) {
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            final List<Order> filteredOrder = getFilteredOrder(nameFilter, params);
            showAll(resp, filteredOrder);
        } else {
            showAll(resp, storage.getAll());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        switch (Objects.requireNonNull(action)) {
            case "canceled":
                storage.canceled(Integer.parseInt(id));
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                showAll(resp, storage.getAll());
                resp.sendRedirect("orders");
                break;
            case "change-status":
                String newStatus = req.getParameter("status");
                storage.changeStatus(Integer.parseInt(id), newStatus);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                showAll(resp, storage.getAll());
                break;
            default:
                showAll(resp, storage.getAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        OrderDTO orderDTO = JsonUtil.readValue(req.getReader().lines().collect(Collectors.joining()), OrderDTO.class);
        if(validationDTO.isValidObjectDTO(resp, orderDTO)){
            Order order = OrderMapper.INSTANCE.getOrder(orderDTO);
            storage.create(order);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.sendRedirect("orders");
        }
    }

    private List<Order> getFilteredOrder(String nameFilter, String params) {
        return switch (nameFilter) {
            case "date" -> storage.filter(Order::getDate, date -> date.isEqual(LocalDate.parse(params)));
            case "status" -> storage.filter(Order::getStatus, status -> status.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
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
