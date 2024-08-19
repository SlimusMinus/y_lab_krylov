package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс для управления конфигурацией базы данных и установлением соединений.
 * <p>
 * Класс загружает настройки базы данных из файла свойств и предоставляет методы для получения
 * соединения с базой данных. Также предоставляет возможность динамического изменения
 * конфигурации подключения к базе данных.
 * </p>
 */

public class DatabaseConfig {
    private static Properties properties;
    private static String url;
    private static String login;
    private static String password;

    public static String getPropertiesFilePath() {
        return PROPERTIES_FILE_PATH;
    }

    public static void setPropertiesFilePath(String propertiesFilePath) {
        PROPERTIES_FILE_PATH = propertiesFilePath;
    }

    private static String PROPERTIES_FILE_PATH = "/db/database.properties";

    /**
     * Загружает свойства базы данных из файла.
     * Если свойства уже загружены, повторная загрузка не выполняется.
     */
    private static void loadProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            try (InputStream is = DatabaseConfig.class.getResourceAsStream(PROPERTIES_FILE_PATH)) {
                if (is == null) {
                    throw new IOException("Файл свойств не найден по пути: " + PROPERTIES_FILE_PATH);
                }
                properties.load(is);
            }
        }
    }
    /**
     * Возвращает URL базы данных.
     * Если URL уже установлен с помощью метода {@link #setConnection(String, String, String)},
     * возвращается это значение. Иначе возвращается URL из файла свойств.
     *
     * @return URL базы данных.
     */
    private static String getUrl() {
        return url != null ? url : properties.getProperty("database.url");
    }

    /**
     * Возвращает логин для подключения к базе данных.
     * Если логин уже установлен с помощью метода {@link #setConnection(String, String, String)},
     * возвращается это значение. Иначе возвращается логин из файла свойств.
     *
     * @return логин для подключения к базе данных.
     */
    private static String getLogin() {
        return login != null ? login : properties.getProperty("database.username");
    }

    /**
     * Возвращает пароль для подключения к базе данных.
     * Если пароль уже установлен с помощью метода {@link #setConnection(String, String, String)},
     * возвращается это значение. Иначе возвращается пароль из файла свойств.
     *
     * @return пароль для подключения к базе данных.
     */
    private static String getPassword() {
        return password != null ? password : properties.getProperty("database.password");
    }

    /**
     * Возвращает соединение с базой данных, используя URL, логин и пароль.
     * Настройки загружаются из файла свойств, если они не были заданы явно с помощью
     * метода {@link #setConnection(String, String, String)}.
     *
     * @return соединение с базой данных.
     * @throws SQLException если возникла ошибка при установке соединения.
     * @throws IOException  если произошла ошибка при загрузке файла свойств.
     */
    public static Connection getConnection() throws SQLException, IOException {
        loadProperties();
        return DriverManager.getConnection(getUrl(), getLogin(), getPassword());
    }

    /**
     * Устанавливает параметры соединения с базой данных. Эти значения будут
     * использованы при следующем вызове метода {@link #getConnection()}.
     *
     * @param url      URL базы данных.
     * @param login    логин для подключения к базе данных.
     * @param password пароль для подключения к базе данных.
     */
    public static void setConnection(String url, String login, String password) {
        DatabaseConfig.url = url;
        DatabaseConfig.login = login;
        DatabaseConfig.password = password;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        DatabaseConfig.properties = properties;
    }

    public static void setUrl(String url) {
        DatabaseConfig.url = url;
    }

    public static void setLogin(String login) {
        DatabaseConfig.login = login;
    }

    public static void setPassword(String password) {
        DatabaseConfig.password = password;
    }
}
