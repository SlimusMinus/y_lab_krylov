package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DisplayName("Тестирование класса UserController")
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

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
     * Тестирование получения всех пользователей.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение всех пользователей")
    void getAll() throws Exception {
        UserDTO userDTO =  new UserDTO("Alexandr", 33, "Moscow", null, null);
        List<UserDTO> users = Collections.singletonList(userDTO);

        when(service.getAll()).thenReturn(Collections.singletonList(new User(1, "admin", "admin", "Alexandr", 33, "Moscow", null, null)));
        when(service.getAllDTO(anyList())).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    /**
     * Тестирование получения пользователей с фильтром.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение пользователей с фильтром")
    void getAllFiltered() throws Exception {
        UserDTO userDTO =  new UserDTO("Alexandr", 33, "Moscow", null, null);
        List<UserDTO> users = Collections.singletonList(userDTO);
        when(service.getFilteredUsers(anyString(), anyString())).thenReturn(Collections
                .singletonList(new User(1, "admin", "admin", "Alexandr", 33, "Moscow", null, null)));
        when(service.getAllDTO(anyList())).thenReturn(users);
        mockMvc.perform(get("/users/filter")
                        .param("name-filter", "John")
                        .param("params", "someParams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    /**
     * Тестирование получения пользователей с сортировкой.
     * Проверяет успешный ответ и правильный тип содержимого.
     */
    @Test
    @DisplayName("Получение пользователей с сортировкой")
    void getAllSorted() throws Exception {
        UserDTO userDTO =  new UserDTO("Alexandr", 33, "Moscow", null, null);
        List<UserDTO> users = Collections.singletonList(userDTO);

        when(service.getSortedUsers(anyString())).thenReturn(Collections
                .singletonList(new User(1, "admin", "admin", "Alexandr", 33, "Moscow", null, null)));
        when(service.getAllDTO(anyList())).thenReturn(users);

        mockMvc.perform(get("/users/sort")
                        .param("params", "someParams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    /**
     * Тестирование обновления пользователя.
     * Проверяет успешное обновление пользователя и статус ответа.
     */
    @Test
    @DisplayName("Обновление пользователя")
    void update() throws Exception {
        UserDTO userDTO =  new UserDTO("Alexandr", 33, "Moscow", null, null);

        when(service.isCarValidation(userDTO, 1)).thenReturn(true);

        mockMvc.perform(put("/users")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }
}