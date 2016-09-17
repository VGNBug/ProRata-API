package com.pawsey.prorata.api.controller;

import com.pawsey.api.rest.controller.BaseRestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * Base class for controller integration tests. Includes configuration for DBUnit database population.
 */
public abstract class BaseControllerIntegrationTest
{
    protected final Log LOGGER = LogFactory.getLog(this.getClass());
    protected final String API_URL = "https://localhost:8080";

    protected RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.datasource.url}") private final String JDBC_URL = "jdbc:postgresql://localhost:5432/ProRata";
    @Value("${spring.database.driverClassName}") private final String DRIVER = "org.postgresql.Driver";
    @Value("${spring.datasource.username}") private final String USER = "postgres";
    @Value("${spring.datasource.password}") private final String PASSWORD = "postgres";

    @Before
    public void importDataSet() throws Exception
    {
        IDataSet dataSet = readDataSet();
        cleanlyInsertDataset(dataSet);
    }

    private IDataSet readDataSet() throws Exception
    {
        return new FlatXmlDataSetBuilder().build(new File("dataset.xml"));
    }

    private void cleanlyInsertDataset(IDataSet dataSet) throws Exception
    {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }
}
