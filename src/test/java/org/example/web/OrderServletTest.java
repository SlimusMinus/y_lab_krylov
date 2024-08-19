package org.example.web;

import org.example.dto.OrderDTO;
import org.example.web.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Класс для тестирования сервлета OrderServlet.
 */
@DisplayName("Тестирование класса OrderServlet")
class OrderServletTest {

    private OrderServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    /**
     * Метод для инициализации тестов, выполняется перед каждым тестом.
     * Создает мок-объекты для HttpServletRequest и HttpServletResponse,
     * а также инициализирует сервлет OrderServlet.
     */
    @BeforeEach
    @DisplayName("Инициализация объектов перед каждым тестом")
    void setUpTest() throws ServletException, IOException {
        servlet = new OrderServlet();
        servlet.init();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    /**
     * Тестирование метода doGet.
     * Проверяет, что запрос обрабатывается успешно и возвращает ожидаемый JSON с информацией о заказах.
     */
    @Test
    @DisplayName("Тестирование метода doGet")
    void doGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertThat(jsonResponse).contains("заказ оформлен");
        assertThat(jsonResponse).contains("canceled");

    }

    /**
     * Тестирование метода doPut.
     * Проверяет, что статус заказа успешно изменяется на "canceled" и возвращается актуальный список заказов.
     */
    @Test
    @DisplayName("Тестирование метода doPut")
    void doPut() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("canceled");
        when(request.getParameter("id")).thenReturn("1");
        servlet.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Тестирование метода doPost.
     * Проверяет, что новый заказ успешно создается и сервер отвечает корректным статусом.
     */
    @Test
    @DisplayName("Тестирование метода doPost")
    void doPost() throws IOException, ServletException {
        OrderDTO newOrderDTO = new OrderDTO(4, 1, LocalDate.parse("2024-08-12"), "заказ оформлен");
        String jsonOrder = JsonUtil.writeValue(newOrderDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonOrder)));
        when(request.getParameter("action")).thenReturn(null); // без действия, чтобы выполнить создание заказа
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).sendRedirect("orders");
    }
}