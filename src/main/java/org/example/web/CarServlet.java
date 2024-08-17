package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.CarDTO;
import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.repository.CarStorage;
import org.example.repository.jdbc.CarStorageJdbc;
import org.example.util.NotFoundException;
import org.example.util.ValidationDTO;
import org.example.web.json.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet для управления объектами {@link Car}.
 * Обрабатывает запросы на отображение, фильтрацию, добавление, редактирование и удаление автомобилей.
 */
@Slf4j
public class CarServlet extends HttpServlet {
    private CarStorage storage;
    private ValidationDTO validationDTO;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = new CarStorageJdbc();
        validationDTO = new ValidationDTO();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        if (action != null && action.equals("filter")) {
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            showAll(resp, getFilteredCars(nameFilter, params));
        } else {
            showAll(resp, storage.getAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = 0;
        resp.setStatus(HttpServletResponse.SC_CREATED);
        saveOrUpdateCar(req, resp, id);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        resp.setStatus(HttpServletResponse.SC_OK);
        saveOrUpdateCar(req, resp, id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        storage.delete(Integer.parseInt(id));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.sendRedirect("cars");
    }

    private List<Car> getFilteredCars(String nameFilter, String params) {
        return switch (nameFilter) {
            case "brand" -> storage.filter(Car::getBrand, brand -> brand.equals(params));
            case "condition" -> storage.filter(Car::getCondition, condition -> condition.equals(params));
            case "price" -> storage.filter(Car::getPrice, price -> price == (Integer.parseInt(params)));
            default -> throw new NotFoundException("Unexpected value: " + nameFilter);
        };
    }

    private void showAll(HttpServletResponse resp, List<Car> cars) throws IOException {
        final List<CarDTO> all = cars.stream()
                .map(CarMapper.INSTANCE::getCar)
                .collect(Collectors.toList());
        String jsonResponse = JsonUtil.writeValue(all);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse);
    }

    private void saveOrUpdateCar(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        CarDTO carDTO = JsonUtil.readValue(req.getReader().lines().collect(Collectors.joining()), CarDTO.class);
        if (validationDTO.isValidObjectDTO(resp, carDTO)) {
            Car newCar = CarMapper.INSTANCE.getCarDTO(carDTO);
            newCar.setId(id);
            storage.saveOrUpdate(newCar);
            resp.setContentType("application/json");
            resp.sendRedirect("cars");
        }
    }
}
