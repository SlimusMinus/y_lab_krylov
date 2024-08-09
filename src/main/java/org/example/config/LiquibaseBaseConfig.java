package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class LiquibaseBaseConfig {
    private static final LiquibaseBaseConfig INSTANCE = new LiquibaseBaseConfig();
    private static Properties properties;

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

    private String getChangelogPath(){
        if(properties ==null){
            properties =new Properties();
            try (InputStream is = new FileInputStream(DatabaseConfig.getPROPERTIES_FILE_PATH())){
                properties.load(is);
            } catch (IOException e) {
                log.error("Error loading properties file: " + e);
            }
        }
        return properties.getProperty("liquibase.changelogPath");
    }

    public static LiquibaseBaseConfig get() {
        return INSTANCE;
    }

}
