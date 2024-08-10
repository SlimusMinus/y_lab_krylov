package org.example.dataTest;

import org.example.model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс {@code Orders} содержит предопределённые объекты заказов и связанные с ними данные.
 * <p>
 * Этот класс используется для хранения и управления коллекцией заказов, таких как список заказов,
 * статусы заказов, а также идентификаторы заказов.
 * </p>
 */
public class Orders {
    public static int OrderId = 1;
    public static final int NOT_EXIST_ID = 800;
    public static final Order order1 = new Order(OrderId++, 4, 1, LocalDate.parse("2024-08-12"), "заказ оформлен");
    public static final Order order2 = new Order(OrderId++, 4, 2, LocalDate.parse("2024-08-13"), "готов к выдаче");
    public static final Order newOrder = new Order(OrderId++, 3, 5, LocalDate.parse("2024-10-10"), "successfully");
    public static final int NEW_SIZE = 3;
    public static final String newStatus = "is ready";
    public static final String canceledStatus = "canceled";
    public static List<Order> listOrder = List.of(order1, order2);
    public static final Map<Integer, Order> orders = new HashMap<>();

    static {
        orders.put(order1.getOrderId(), order1);
        orders.put(order2.getOrderId(), order2);
    }
}
