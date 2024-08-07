package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseBaseConfig {
    private static final String changelogPath = "db/changelog/changelog-master.xml";
    private static final LiquibaseBaseConfig INSTANCE = new LiquibaseBaseConfig();

    private LiquibaseBaseConfig(){
        try {
            try (Connection connection = DatabaseConfig.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database);
                liquibase.clearCheckSums();
                liquibase.update();
                System.out.println("Migration is successfully");
            } catch (SQLException | LiquibaseException exception) {
                System.out.println("SQL got exception " + exception.getMessage());
            }

        } catch (IOException ex) {
            System.out.println("Error loading properties file: " + ex.getMessage());
        }
    }

    public static LiquibaseBaseConfig get() {
        return INSTANCE;
    }

}
