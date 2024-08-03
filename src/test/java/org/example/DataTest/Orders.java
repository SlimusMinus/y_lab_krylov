package org.example.DataTest;

import org.example.model.Order;
import org.example.repository.CarData;
import org.example.repository.UserData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders {
    public static int OrderId = 1;
    public static final Order order1 = new Order(OrderId++, LocalDate.parse("2024-12-12"),
            CarData.getCars().get(1), UserData.getUsers().get(3), "successfully");
    public static final Order order2 = new Order(OrderId++, LocalDate.parse("2024-11-11"),
            CarData.getCars().get(2), UserData.getUsers().get(4), "successfully");
    public static final Order newOrder = new Order(OrderId++, LocalDate.parse("2024-10-10"),
            CarData.getCars().get(3), UserData.getUsers().get(2), "successfully");
    public static final String newStatus = "is ready";

    public static List<Order> listOrder = List.of(order1, order2);

    public static final Map<Integer, Order> orders = new HashMap<>();
    static {
        orders.put(order1.getOrderId(), order1);
        orders.put(order2.getOrderId(), order2);
    }
}
