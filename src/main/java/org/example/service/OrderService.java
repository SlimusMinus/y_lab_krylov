package org.example.service;

import org.example.dto.OrderDTO;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.util.NotFoundException;
import org.example.util.ObjectValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final OrderStorage storage;
    private final ObjectValidator objectValidator;

    public OrderService(OrderStorage storage, ObjectValidator objectValidator) {
        this.storage = storage;
        this.objectValidator = objectValidator;
    }

    public void create(Order order) {
        storage.create(order);
    }

    public List<Order> getAll() {
        return storage.getAll();
    }

    public List<OrderDTO> getAllDTO(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper.INSTANCE::getOdderDTO)
                .toList();
    }

    public Order getById(int id) {
        return storage.getById(id);
    }

    public void changeStatus(int id, String status) {
        storage.changeStatus(id, status);
    }

    public void canceled(int id) {
        storage.canceled(id);
    }

    public List<Order> getFilteredOrder(String nameFilter, String params) {
        return switch (nameFilter) {
            case "date" -> storage.filter(Order::getDate, date -> date.isEqual(LocalDate.parse(params)));
            case "status" -> storage.filter(Order::getStatus, status -> status.equals(params));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

    public boolean isOrderValidation(OrderDTO orderDTO) {
        if (objectValidator.isValidObjectDTO(orderDTO)) {
            Order order = OrderMapper.INSTANCE.getOrder(orderDTO);
            storage.create(order);
            return true;
        } else {
            return false;
        }
    }
}
