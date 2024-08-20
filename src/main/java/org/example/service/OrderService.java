package org.example.service;

import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.repository.jdbc.OrderStorageJdbc;
import org.example.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final OrderStorage orderStorage;

    public OrderService(OrderStorage orderStorage) {
        this.orderStorage = orderStorage;
    }

    public void create(Order order) {
        orderStorage.create(order);
    }

    public List<Order> getAll() {
        return orderStorage.getAll();
    }

    public Order getById(int id) {
        return orderStorage.getById(id);
    }

    public void changeStatus(int id, String status) {
        orderStorage.changeStatus(id, status);
    }

    public void canceled(int id) {
        orderStorage.canceled(id);
    }

    public List<Order> getFilteredOrder(String nameFilter, String params) {
        return switch (nameFilter) {
            case "date" -> orderStorage.filter(Order::getDate, date -> date.isEqual(LocalDate.parse(params)));
            case "status" -> orderStorage.filter(Order::getStatus, status -> status.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }
}
