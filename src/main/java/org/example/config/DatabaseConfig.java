package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties;
    private static final String propertiesFilePath = "src/main/resources/db/database.properties";
    private static void loadProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            try (InputStream is = new FileInputStream(DatabaseConfig.propertiesFilePath)) {
                properties.load(is);
            }
        }
    }
    private static String getUrl() {
        return properties.getProperty("database.url");
    }

    private static String getLogin() {
        return properties.getProperty("database.username");
    }

    private static String getPassword() {
        return properties.getProperty("database.password");
    }

    public static Connection getConnection() throws SQLException, IOException {
        loadProperties();
        return DriverManager.getConnection(getUrl(), getLogin(), getPassword());
    }
}
