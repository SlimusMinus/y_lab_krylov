package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDTO;
import org.example.repository.jdbc.AbstractStorageJdbcTest;
import org.example.web.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@Slf4j
@DisplayName("Тестирование класса UserServlet")
class UserServletTest extends AbstractStorageJdbcTest {

    private UserServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    @DisplayName("Инициализация объектов перед каждым тестом")
    void setUpTest() throws ServletException, IOException {
        servlet = new UserServlet();
        servlet.init();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void doGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertThat(jsonResponse).contains("Alexandr");
        assertThat(jsonResponse).contains("John");
    }

    @Test
    void doPut() throws IOException, ServletException {
        UserDTO updateUserDTO = new UserDTO("Alexandr", 35, "Moscow", null, null);
        String jsonUser = JsonUtil.writeValue(updateUserDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonUser)));
        when(request.getParameter("id")).thenReturn("1");
        servlet.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).sendRedirect("users");
    }

    /**
     * Скрипт для создания таблицы пользователей в testContainer
     */
    @Override
    protected String createTable() {
        return """
                DROP TABLE IF EXISTS car_shop.user;
                CREATE TABLE car_shop.user(
                user_id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT,
                name TEXT,
                age INTEGER,
                city TEXT)""";
    }

    /**
     * Скрипт для заполнения таблицы пользователей в testContainer
     */
    @Override
    protected String populateTable() {
        return """
                INSERT INTO car_shop.user (login,password,name,age,city) VALUES
                ('admin','admin','Alexandr',33,'Moscow'),
                ('manager1','manager1','John',36,'New-York'),
                ('manager2','manager2','Alexandr',34,'Moscow'),
                ('client1','client1','Tanya',25,'London'),
                ('client2','client2','Valera',45,'Milan'),
                ('client3','client3','Robert',33,'Moscow')
                """;
    }

}