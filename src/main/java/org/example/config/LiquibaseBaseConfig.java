package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс для конфигурации и запуска Liquibase миграций базы данных.
 * <p>
 * Этот класс реализует паттерн Singleton и автоматически выполняет миграции базы данных
 * при его создании. Он использует конфигурацию базы данных из {@link DatabaseConfig}.
 * </p>
 */
@Slf4j
public class LiquibaseBaseConfig {
    private static final LiquibaseBaseConfig INSTANCE = new LiquibaseBaseConfig();
    private static Properties properties;

    /**
     * Приватный конструктор класса, который выполняет миграции базы данных
     * с использованием Liquibase при создании объекта.
     * <p>
     * Конструктор загружает конфигурацию базы данных, инициализирует Liquibase
     * и выполняет обновление схемы базы данных на основе указанного changelog.
     * </p>
     */
    private LiquibaseBaseConfig(){
        try {
            try (Connection connection = DatabaseConfig.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(getChangelogPath(), new ClassLoaderResourceAccessor(), database);
                liquibase.clearCheckSums();
                liquibase.update();
                log.info("Migration is successfully");
            } catch (SQLException | LiquibaseException exception) {
                log.error("SQL got exception " + exception);
            }

        } catch (IOException ex) {
            log.error("Error loading properties file: " + ex);
        }
    }

    /**
     * Возвращает путь к файлу changelog для Liquibase.
     * <p>
     * Метод загружает файл свойств, если он еще не был загружен, и извлекает путь к changelog
     * из файла свойств.
     * </p>
     *
     * @return путь к файлу changelog.
     */
    private String getChangelogPath(){
        if(properties ==null){
            properties =new Properties();
            try (InputStream is = new FileInputStream(DatabaseConfig.getPropertiesFilePath())){
                properties.load(is);
            } catch (IOException e) {
                log.error("Error loading properties file: " + e);
            }
        }
        return properties.getProperty("liquibase.changelogPath");
    }
    /**
     * Возвращает единственный экземпляр класса {@link LiquibaseBaseConfig}.
     * <p>
     * Используется для получения доступа к экземпляру и выполнения миграций
     * в других частях программы.
     * </p>
     *
     * @return экземпляр класса {@link LiquibaseBaseConfig}.
     */
    public static LiquibaseBaseConfig get() {
        return INSTANCE;
    }

}
