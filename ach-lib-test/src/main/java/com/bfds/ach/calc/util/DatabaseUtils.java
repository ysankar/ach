package com.bfds.ach.calc.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseUtils {

    @Value("${database.schema.suffix}")
    private String databaseSchemaSuffix;

    @Autowired
    private DataSource dataSource;

    public void executeScriptFile(String classPathResource, DataSource dataSource) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource(classPathResource));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }

    public void createTestDate(Class<?> clazz, DataSource dataSource) {
        String filePath = clazz.getName().replaceAll("\\.", "/") + "-" + databaseSchemaSuffix + ".sql";
        executeScriptFile(filePath);
    }

    public void deleteTestDate(Class<?> clazz, DataSource dataSource) {
        String filePath = clazz.getName().replaceAll("\\.", "/") + "-delete-" + databaseSchemaSuffix + ".sql";
        executeScriptFile(filePath);
    }

    public void executeScriptFile(String classPathResource) {
        executeScriptFile(classPathResource, dataSource);
    }

    public void createTestDate(Class<?> clazz) {
        createTestDate(clazz, dataSource);
    }

    public void deleteTestDate(Class<?> clazz) {
        deleteTestDate(clazz, dataSource);
    }
}
