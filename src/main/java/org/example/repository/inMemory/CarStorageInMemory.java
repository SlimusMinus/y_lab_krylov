package org.example.repository.inMemory;

import org.example.model.Car;
import org.example.repository.inMemory.data.CarData;
import org.example.repository.CarStorage;
import org.example.service.authentication.AuthServiceInMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализация сервиса управления автомобилями.
 *
 * <p>Этот класс предоставляет конкретные реализации методов интерфейса {@link CarStorage} для управления данными автомобилей.
 * Он позволяет получать список всех автомобилей, сохранять или обновлять данные об автомобиле, удалять автомобиль по идентификатору
 * и фильтровать автомобили по заданным критериям.</p>
 */
public class CarStorageInMemory implements CarStorage {

    private CarData carData = new CarData();

    private static final Logger log = LoggerFactory.getLogger(AuthServiceInMemory.class);

    /**
     * Получает список всех автомобилей.
     *
     * <p>Этот метод возвращает все автомобили, хранящиеся в {@link CarData}. Логирует операцию получения всех автомобилей.</p>
     *
     * @return Список объектов {@link Car}, представляющих все автомобили в системе.
     */
    @Override
    public List<Car> getAll() {
        log.info("Get all cars");
        return carData.getCars().values().stream().toList();
    }

    @Override
    public Car getById(int id){
        return null;
    }

    /**
     * Сохраняет новый автомобиль или обновляет данные об существующем автомобиле.
     *
     * <p>Если у автомобиля идентификатор равен 0, считается, что это новый автомобиль, и ему присваивается новый уникальный идентификатор.
     * В противном случае данные об автомобиле обновляются. Логирует операцию добавления или обновления автомобиля.</p>
     *
     * @param car Объект {@link Car}, содержащий информацию об автомобиле для сохранения или обновления.
     * @return Сохраненный или обновленный объект {@link Car}.
     */
    @Override
    public Car saveOrUpdate(Car car) {
        if (car.getId() == 0) {
            log.info("Car {} added", car);
            int id = CarData.getCarId()+1;
            car.setId(id);
            carData.getCars().put(id, car);
        } else {
            log.info("Car {} updated", car);
            carData.getCars().put(car.getId(), car);
        }
        return car;
    }

    /**
     * Удаляет автомобиль по идентификатору.
     *
     * <p>Удаляет автомобиль из {@link CarData} по его уникальному идентификатору. Логирует операцию удаления автомобиля.</p>
     *
     * @param id Идентификатор автомобиля, который нужно удалить.
     */
    @Override
    public void delete(int id) {
        log.info("Car with id - {} removed", id);
        carData.getCars().remove(id);
    }

    /**
     * Фильтрует список автомобилей по заданному критерию.
     *
     * <p>Этот метод позволяет отфильтровать автомобили на основе функции получения свойства и предиката для проверки этого свойства.
     * Логирует операцию фильтрации автомобилей.</p>
     *
     * @param <T> Тип свойства, по которому производится фильтрация.
     * @param getter Функция, возвращающая значение свойства {@link Car}, по которому будет происходить фильтрация.
     * @param predicate Предикат, проверяющий, соответствует ли значение свойства заданному условию.
     * @return Список объектов {@link Car}, которые удовлетворяют заданному критерию фильтрации.
     */
    @Override
    public <T> List<Car> filter(Function<Car, T> getter, Predicate<T> predicate) {
        log.info("Get all find cars");
        List<Car> list = carData.getCars().values().stream().toList();
        return list.stream().filter(car -> predicate.test(getter.apply(car))).toList();
    }
}
