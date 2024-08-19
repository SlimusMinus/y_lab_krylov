package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("Тестирование класса UserServlet")
class UserServletTest {

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
}