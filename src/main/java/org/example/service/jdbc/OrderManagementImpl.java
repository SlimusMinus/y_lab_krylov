package org.example.service.jdbc;

import org.example.model.Order;
import org.example.service.OrderManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class OrderManagementImpl implements OrderManagement {

    private static final Logger log = LoggerFactory.getLogger(OrderManagementImpl.class);


    @Override
    public void create(Order order) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public Order getById(int id) {
        return null;
    }

    @Override
    public void changeStatus(int id, String status) {

    }

    @Override
    public void canceled(int id) {

    }

    @Override
    public <T> List<Order> filter(Function<Order, T> getter, Predicate<T> predicate) {
        return null;
    }
}
