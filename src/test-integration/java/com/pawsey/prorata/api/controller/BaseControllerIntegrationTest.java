package com.pawsey.prorata.api.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Base class for controller integration tests. Includes configuration for DBUnit database population.
 *
 * @param <T> The entity acted upon by the controller being tested by an inheritor of this class.
 */
public abstract class BaseControllerIntegrationTest<T> {

    protected final Log LOGGER = LogFactory.getLog(this.getClass());
    protected final String API_URL = "http://localhost:8080";

    protected RestTemplate restTemplate = new RestTemplate();

    private T baseEntityExemplar;

    @Value("${spring.datasource.url}")
    private  String JDBC_URL;
    @Value("${spring.datasource.driverClassName}")
    private  String DRIVER;
    @Value("${spring.datasource.username}")
    private  String USER;
    @Value("${spring.datasource.password}")
    private  String PASSWORD;

    /**
     * The {@link BaseRestController#create(Map)} method relies on fields unique to
     * entities, so must be implemented by tests themselves.
     */
    protected abstract void testCreate();

    protected abstract void testRead();

    /**
     * The {@link BaseRestController#update(Map)} method relies on fields unique to
     * entities, so must be implemented by tests themselves.
     */
    protected abstract void testUpdate();

    protected abstract void testDelete();

    @Before
    public void importDataSet() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanlyInsertDataset(dataSet);
    }

    protected T makeReadRequest(final String controllerPath, Integer idParam) {
        assertNotNull("controllerPath cannot be null", controllerPath);
        assertNotNull("idParam cannot be null", idParam);

        final String url = API_URL + "/" + controllerPath + "/" + idParam.toString();
        T response = null;
        try {
            response = restTemplate.getForObject(new URI(url), (Class<T>) baseEntityExemplar.getClass());
        } catch (URISyntaxException e) {
            LOGGER.error(e);
        }
        LOGGER.info("GET for a " + response.getClass().getSimpleName() + " made at " + url);
        return response;
    }

    protected void makeDeleteRequest(final String controllerPath, final Integer idParam) {
        assertNotNull("controllerPath cannot be null", controllerPath);

        String url = API_URL + "/" + controllerPath + idParam.toString();

        try {
            restTemplate.delete(url);
            LOGGER.info("successful delete request made at " + url);
        } catch (RestClientException e) {
            String failNotice = "delete request made at " + url + " failed";
            LOGGER.error(failNotice);
            fail(failNotice);
        }
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new File("src/test-integration/resources/dataset.xml"));
    }

    private void cleanlyInsertDataset(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

}

