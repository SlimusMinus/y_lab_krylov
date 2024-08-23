package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.OrderDTO;
import org.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DisplayName("Тестирование класса OrderController")
class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Настройка тестовой среды перед каждым тестом.
     * Инициализирует моки и настраивает MockMvc.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Тестирование получения всех заказов.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение всех заказов")
    void getAll() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование получения заказов с фильтром.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение заказов с фильтром")
    void getAllAfterFilter() throws Exception {
        mockMvc.perform(get("/orders/filter")
                        .param("name-filter", "example")
                        .param("params", "someParams"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование получения заказа по идентификатору.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение заказа по ID")
    void getById() throws Exception {
        mockMvc.perform(get("/orders/1")).andExpect(status().isOk());
    }

    /**
     * Тестирование создания нового заказа.
     * Проверяет успешное создание заказа и статус ответа.
     */
    @Test
    @DisplayName("Создание нового заказа")
    void create() throws Exception {
        OrderDTO orderDTO = new OrderDTO(4, 1, LocalDate.parse("2024-08-12"), "заказ оформлен");
        when(orderService.isOrderValidation(orderDTO)).thenReturn(true);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated());
    }

    /**
     * Тестирование отмены заказа.
     * Проверяет успешное выполнение отмены заказа и статус ответа.
     */
    @Test
    @DisplayName("Отмена заказа")
    void canceled() throws Exception {
        mockMvc.perform(put("/orders/canceled")
                        .param("id", "1"))
                .andExpect(status().isCreated());
    }

    /**
     * Тестирование изменения статуса заказа.
     * Проверяет успешное изменение статуса заказа и статус ответа.
     */
    @Test
    @DisplayName("Изменение статуса заказа")
    void changeStatus() throws Exception {
        mockMvc.perform(put("/orders/change-status")
                        .param("id", "1")
                        .param("status", "Shipped"))
                .andExpect(status().isCreated());
    }
}