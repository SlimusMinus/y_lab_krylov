package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aop.UserAuditAspect;
import org.example.repository.AbstractStorageTest;
import org.example.repository.CarStorage;
import org.example.repository.OrderStorage;
import org.example.repository.UserStorage;
import org.example.service.CarService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.util.ObjectValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class AppConfigTest {

    @Bean(name = "dataSourceTest")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(AbstractStorageTest.getPostgresContainer().getDriverClassName());
        dataSource.setUrl(AbstractStorageTest.getPostgresContainer().getJdbcUrl());
        dataSource.setUsername(AbstractStorageTest.getPostgresContainer().getUsername());
        dataSource.setPassword(AbstractStorageTest.getPostgresContainer().getPassword());
        return dataSource;
    }

    @Bean(name = "jdbcTemplateTest")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean(name = "namedParameterJdbcTemplateTest")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean(name = "objectMapperTest")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean(name = "carStorageTest")
    public CarStorage carStorage() {
        return new CarStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean(name = "orderStorageTest")
    public OrderStorage orderStorage() {
        return new OrderStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean(name = "userStorageTest")
    public UserStorage userStorage() {
        return new UserStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean(name = "carServiceTest")
    public CarService carService() {
        return new CarService(carStorage(), objectValidator());
    }

    @Bean(name = "orderServiceTest")
    public OrderService orderService() {
        return new OrderService(orderStorage(), objectValidator());
    }

    @Bean(name = "userServiceTest")
    public UserService userService() {
        return new UserService(userStorage(), objectValidator());
    }

    @Bean(name = "loggingAspectTest")
    public UserAuditAspect loggingAspect() {
        return new UserAuditAspect();
    }

    @Bean(name = "objectValidatorTest")
    public ObjectValidator objectValidator() {
        return new ObjectValidator();
    }
}

