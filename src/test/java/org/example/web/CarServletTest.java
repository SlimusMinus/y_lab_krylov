package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Car;
import org.example.web.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Cars.newCarDTO;
import static org.example.dataTest.Cars.updatedCarDTO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link CarServlet}.
 * <p>
 * Этот класс содержит тесты для проверки работы сервлета CarServlet, который управляет объектами {@link Car}.
 * Тестируется функциональность методов doGet, doPost, doPut и doDelete.
 * </p>
 */
@Slf4j
@DisplayName("Тестирование класса CarServlet")
class CarServletTest {

    private CarServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    /**
     * Инициализация тестов.
     * <p>
     * Создаёт экземпляр CarServlet, моки для HttpServletRequest и HttpServletResponse, а также настраивает
     * BufferedWriter для проверки ответа.
     * </p>
     *
     * @throws ServletException если возникает ошибка при инициализации сервлета
     * @throws IOException если возникает ошибка при создании BufferedWriter
     */
    @BeforeEach
    void setUpTest() throws ServletException, IOException {
        servlet = new CarServlet();
        servlet.init();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    /**
     * Тестирование метода doGet.
     * <p>
     * Проверяет, что метод doGet устанавливает статус ответа 200 OK и содержит ожидаемые данные в JSON-ответе.
     * </p>
     *
     * @throws ServletException если возникает ошибка при выполнении doGet
     * @throws IOException если возникает ошибка при чтении ответа
     */
    @Test
    @DisplayName("Тестирование метода doGet ")
    void doGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Тестирование метода doPost.
     * <p>
     * Проверяет, что метод doPost устанавливает статус ответа 201 Created.
     * </p>
     *
     * @throws ServletException если возникает ошибка при выполнении doPost
     * @throws IOException если возникает ошибка при чтении ответа
     */
    @Test
    @DisplayName("Тестирование метода doPost")
    void doPost() throws ServletException, IOException {
        String jsonCar = JsonUtil.writeValue(newCarDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonCar)));
        when(request.getParameter("id")).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Тестирование метода doPut.
     * <p>
     * Проверяет, что метод doPut устанавливает статус ответа 200 OK.
     * </p>
     *
     * @throws ServletException если возникает ошибка при выполнении doPut
     * @throws IOException если возникает ошибка при чтении ответа
     */
    @Test
    @DisplayName("Тестирование метода doPut")
    void doPut() throws ServletException, IOException {
        String jsonCar = JsonUtil.writeValue(updatedCarDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonCar)));
        when(request.getParameter("id")).thenReturn("1");
        servlet.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Тестирование метода doDelete.
     * <p>
     * Проверяет, что метод doDelete устанавливает статус ответа 204 No Content.
     * </p>
     *
     * @throws ServletException если возникает ошибка при выполнении doDelete
     * @throws IOException если возникает ошибка при чтении ответа
     */
    @Test
    @DisplayName("Тестирование метода doDelete")
    void doDelete() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("15");
        servlet.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}