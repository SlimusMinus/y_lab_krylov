package org.example.repository;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, содержащий статические данные о заказах.
 *
 * <p>Этот класс предоставляет статические данные о различных заказах, включая их идентификаторы, даты, связанные автомобили,
 * пользователей и статусы заказов. Он инициализирует коллекцию {@link Map} для хранения информации о каждом заказе, где ключом является уникальный
 * идентификатор заказа, а значением - объект {@link Order}.</p>
 *
 */
public class OrderData {
    /**
     * Текущий идентификатор заказа, используемый для создания новых заказов.
     *
     * <p>Этот идентификатор автоматически увеличивается при создании нового заказа.</p>
     */
    @Getter
    private static int OrderId = 1;
    private static final Order order1 = new Order(OrderId++, LocalDate.parse("2024-12-12"),
            CarData.getCars().get(1), UserData.getUsers().get(3), "successfully");
    private static final Order order2 = new Order(OrderId++, LocalDate.parse("2024-11-11"),
            CarData.getCars().get(2), UserData.getUsers().get(4), "successfully");

    /**
     * Коллекция для хранения информации о всех заказах.
     *
     * <p>Ключом является уникальный идентификатор заказа, а значением - объект {@link Order}.</p>
     */
    @Getter
    @Setter
    private static Map<Integer, Order> orders = new HashMap<>();

    static {
        orders.put(order1.getOrderId(), order1);
        orders.put(order2.getOrderId(), order2);
    }
}
