package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarDTO;
import org.example.service.CarService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DisplayName("Тестирование класса CarController")
class CarControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CarService carService;
    @InjectMocks
    private CarController controller;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Настройка тестовой среды перед каждым тестом.
     * Инициализирует моки и настраивает MockMvc.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Тестирование получения всех автомобилей.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение всех автомобилей")
    void getAll() throws Exception {
        mockMvc.perform(get("/cars"))

                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование получения автомобилей с фильтром.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение автомобилей с фильтром")
    void getAllAfterFilter() throws Exception {
        mockMvc.perform(get("/cars/filter"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование получения автомобиля по идентификатору.
     * Проверяет успешный ответ.
     */
    @Test
    @DisplayName("Получение автомобиля по ID")
    void getById() throws Exception {
        mockMvc.perform(get("/cars/1")).andExpect(status().isOk());
    }

    /**
     * Тестирование создания нового автомобиля.
     * Проверяет успешное создание автомобиля и статус ответа.
     */
    @Test
    @DisplayName("Создание нового автомобиля")
    void create() throws Exception {
        CarDTO carDTO = new CarDTO("Toyota", "Camry", 2022, 15888, "good");
        when(carService.isCarValidation(carDTO, 0)).thenReturn(true);
        mockMvc.perform(post("/cars")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isCreated());
    }

    /**
     * Тестирование обновления автомобиля.
     * Проверяет успешное обновление автомобиля и статус ответа.
     */
    @Test
    @DisplayName("Обновление автомобиля")
    void update() throws Exception {
        CarDTO carDTO = new CarDTO("Toyota", "Camry", 2022, 15888, "good");
        when(carService.isCarValidation(carDTO, 1)).thenReturn(true);
        mockMvc.perform(put("/cars")
                        .param("id", "1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isCreated());
    }

}