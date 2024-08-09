package org.example.repository.inMemory;

import org.example.repository.CarStorage;
import org.example.repository.inMemory.data.OrderData;
import org.example.model.Order;
import org.example.repository.OrderStorage;
import org.example.service.AuthServiceInMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link OrderStorage}, управляющая заказами.
 *
 * <p>Этот класс предоставляет реализацию методов для создания, получения, изменения статуса, отмены и фильтрации заказов.
 * Он использует хранилище заказов, предоставляемое классом {@link OrderData}, и логирует действия через SLF4J.</p>
 */
public class OrderStorageInMemory implements OrderStorage {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceInMemory.class);

    private CarStorage carStorage = new CarStorageInMemory();

    private Map<Integer, Order> orders = OrderData.getOrders();

    /**
     * Создает новый заказ.
     *
     * <p>Этот метод добавляет новый заказ в хранилище заказов и присваивает ему уникальный идентификатор.</p>
     *
     * @param order Объект {@link Order}, представляющий новый заказ.
     * @throws IllegalArgumentException Если объект {@code order} является {@code null}.
     */
    @Override
    public synchronized void create(Order order) {
        if (!isCarAvailable(order.getCarId())) {
            log.warn("Car {} is not available for order", order.getCarId());
            throw new IllegalStateException("Car is not available");
        }
        log.info("Order - {} is created", order);
        int id = OrderData.getOrderId() + 1;
        order.setOrderId(id);
        orders.put(id, order);
    }

    /**
     * Возвращает список всех заказов.
     *
     * <p>Этот метод возвращает все заказы, хранящиеся в хранилище заказов.</p>
     *
     * @return Список всех заказов.
     */
    @Override
    public List<Order> getAll() {
        log.info("Show all orders");
        return orders.values().stream().toList();
    }

    /**
     * Возвращает заказ по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @return Объект {@link Order}, соответствующий указанному идентификатору, или {@code null}, если заказ не найден.
     */
    @Override
    public Order getById(int id) {
        return orders.get(id);
    }

    /**
     * Изменяет статус заказа.
     *
     * @param id     Идентификатор заказа.
     * @param status Новый статус заказа.
     * @throws IllegalArgumentException Если заказ с указанным идентификатором не найден.
     */
    @Override
    public void changeStatus(int id, String status) {
        log.info("Change status on {}, repository order with {} id", status, id);
        Order changeOrder = orders.get(id);
        changeOrder.setStatus(status);
        orders.put(id, changeOrder);
    }

    /**
     * Отменяет заказ.
     *
     * @param id Идентификатор заказа.
     * @throws IllegalArgumentException Если заказ с указанным идентификатором не найден.
     */
    @Override
    public void canceled(int id) {
        log.info("Order with {} id is canceled", id);
        Order canceledOrder = orders.get(id);
        canceledOrder.setStatus("canceled");
        orders.put(id, canceledOrder);
    }

    /**
     * Фильтрует заказы по заданному критерию.
     *
     * @param <T>       Тип возвращаемого значения функции {@code getter}.
     * @param getter    Функция для получения значения, по которому будет производиться фильтрация.
     * @param predicate Условие для фильтрации заказов.
     * @return Список заказов, удовлетворяющих условию фильтрации.
     */
    @Override
    public <T> List<Order> filter(Function<Order, T> getter, Predicate<T> predicate) {
        log.info("Get all find orders");
        return orders.values().stream().filter(user -> predicate.test(getter.apply(user))).toList();
    }

    private boolean isCarAvailable(int carId) {
        for (Order existingOrder : orders.values()) {
            if (existingOrder.getCarId() == carId && !existingOrder.getStatus().equalsIgnoreCase("canceled")) {
                return false;
            }
        }
        return true;
    }
}
