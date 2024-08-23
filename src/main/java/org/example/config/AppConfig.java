package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liquibase.integration.spring.SpringLiquibase;
import org.example.aop.UserAuditAspect;
import org.example.repository.CarStorage;
import org.example.repository.OrderStorage;
import org.example.repository.UserStorage;
import org.example.service.CarService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.util.ObjectValidator;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Objects;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.yaml")
public class AppConfig {

    @Bean
    public YamlPropertiesFactoryBean yamlProperties() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yaml"));
        return yaml;
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        YamlPropertiesFactoryBean yaml = yamlProperties();
        yaml.afterPropertiesSet();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(yaml.getObject()).getProperty("database.driver-class-name"));
        dataSource.setUrl(yaml.getObject().getProperty("database.url"));
        dataSource.setUsername(yaml.getObject().getProperty("database.username"));
        dataSource.setPassword(yaml.getObject().getProperty("database.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:db/changelog/changelog-master.xml");
        return liquibase;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public CarStorage carStorage() {
        return new CarStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean
    public OrderStorage orderStorage() {
        return new OrderStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean
    public UserStorage userStorage() {
        return new UserStorage(jdbcTemplate(), namedParameterJdbcTemplate());
    }

    @Bean
    public CarService carService() {
        return new CarService(carStorage(), objectValidator());
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(orderStorage(), objectValidator());
    }

    @Bean
    public UserService userService() {
        return new UserService(userStorage(), objectValidator());
    }

    @Bean
    public UserAuditAspect loggingAspect() {
        return new UserAuditAspect();
    }

    @Bean
    public ObjectValidator objectValidator() {
        return new ObjectValidator();
    }
}
