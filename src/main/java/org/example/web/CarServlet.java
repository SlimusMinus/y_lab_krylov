package org.example.web;

import org.example.dto.CarDTO;
import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.service.CarService;
import org.example.util.ObjectValidator;
import org.example.web.json.JsonUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class CarServlet extends HttpServlet {
    private ConfigurableApplicationContext springContext;
    private CarService service;
    private ObjectValidator objectValidator;

    @Override
    public void init() throws ServletException {
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        service = springContext.getBean(CarService.class);
        objectValidator = new ObjectValidator();
        super.init();
    }

    @Override
    public void destroy() {
        springContext.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String action = req.getParameter("action");
        if (action != null && action.equals("filter")) {
            String nameFilter = req.getParameter("name-filter");
            String params = req.getParameter("params");
            showAll(resp, service.getFilteredCars(nameFilter, params));
        } else {
            showAll(resp, service.getAll());
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
        service.delete(Integer.parseInt(id));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.sendRedirect("cars");
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
        if (objectValidator.isValidObjectDTO(resp, carDTO)) {
            Car newCar = CarMapper.INSTANCE.getCarDTO(carDTO);
            newCar.setCar_id(id);
            service.saveOrUpdate(newCar);
            resp.setContentType("application/json");
            resp.sendRedirect("cars");
        }
    }
}
