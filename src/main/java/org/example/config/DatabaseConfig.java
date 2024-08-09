package org.example.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseConfig {
    private static Properties properties;
    private static String url;
    private static String login;
    private static String password;

    @Getter
    private static final String PROPERTIES_FILE_PATH = "src/main/resources/db/database.properties";

    private static void loadProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            try (InputStream is = new FileInputStream(PROPERTIES_FILE_PATH)) {
                properties.load(is);
            }
        }
    }

    private static String getUrl() {
        return url != null ? url : properties.getProperty("database.url");
    }

    private static String getLogin() {
        return login != null ? login : properties.getProperty("database.username");
    }

    private static String getPassword() {
        return password != null ? password : properties.getProperty("database.password");
    }

    public static Connection getConnection() throws SQLException, IOException {
        loadProperties();
        return DriverManager.getConnection(getUrl(), getLogin(), getPassword());
    }

    public static void setConnection(String url, String login, String password) {
        DatabaseConfig.url = url;
        DatabaseConfig.login = login;
        DatabaseConfig.password = password;
    }

}
