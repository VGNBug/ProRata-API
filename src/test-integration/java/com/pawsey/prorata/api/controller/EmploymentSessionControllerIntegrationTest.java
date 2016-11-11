package com.pawsey.prorata.api.controller;

import com.pawsey.prorata.api.ProRataApiApplication;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProRataApiApplication.class)
@WebAppConfiguration
@IntegrationTest("${local.server.port}")
public class EmploymentSessionControllerIntegrationTest extends BaseControllerIntegrationTest {

    private final String CONTROLLER_PATH = "/employmentSession";
    private final String HAPPY_PATH_EMAIL = "bob@test.com";
    private final String HAPPY_PATH_PASSWORD = "password";
    private final String SAD_PATH_EMAIL = "sadPathEmail@test.com";
    private final String SAD_PATH_PASSWORD = "sadPathPassword";

    private EmploymentSessionEntity expected;

    @Before
    public void setup() {
        Date now = Calendar.getInstance().getTime();

        ProrataUserEntity user = new ProrataUserEntity();
        user.setProrataUserId(1);
        user.setEmail(HAPPY_PATH_EMAIL);
        user.setPassword(HAPPY_PATH_PASSWORD);

        EmployerEntity employer = new EmployerEntity();
        employer.setEmployerId(1);
        employer.setOfficeAddress("1 Office Lane, Testington, Testinghamshire");
        employer.setOfficePostcode("TE1 2ST");

        EmploymentEntity employment = new EmploymentEntity();
        employment.setEmploymentId(1);
        employment.setName("Test employment");
        employment.setProrataUser(user);
        employment.setEmployer(employer);
        employment.setHourlyRate(new BigDecimal(10));
        employment.setHoursPerWeek(new BigDecimal(40));
        employment.setStartDate(now);

        LocationEntity location = new LocationEntity();
        location.setLocationId(1);
        location.setXCoordinate(new BigDecimal(0));
        location.setYCoordinate(new BigDecimal(0));

        expected = new EmploymentSessionEntity();
        expected.setEmployment(employment);
        expected.setEmploymentSessionId(1);
        expected.setStartTime(now);
        expected.setEndTime(now);
        expected.setLocation(location);
    }

    @Override
    @Test
    public void testCreate() {
        runTestCreateForSuccess(API_URL + CONTROLLER_PATH + "/" + HAPPY_PATH_EMAIL + "/" + HAPPY_PATH_PASSWORD, HttpStatus.CREATED, expected);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testCreateShouldFailWithRightEmailWrongPassword() {
        runTestCreateForFailure(API_URL + CONTROLLER_PATH + "/" + HAPPY_PATH_EMAIL + "/" + SAD_PATH_PASSWORD, HttpStatus.UNAUTHORIZED, expected);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testCreateShouldFailWithWrongEmailWrongPassword() {
        runTestCreateForFailure(API_URL + CONTROLLER_PATH + "/" + SAD_PATH_EMAIL + "/" + HAPPY_PATH_PASSWORD, HttpStatus.UNAUTHORIZED, expected);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testCreateShouldFailWithNoData() {
        runTestCreateForFailure(API_URL + CONTROLLER_PATH + "/" + HAPPY_PATH_EMAIL + "/" + HAPPY_PATH_PASSWORD, HttpStatus.NOT_ACCEPTABLE, null);
    }

    @Override
    protected void testRead() {

    }

    @Override
    protected void testUpdate() {

    }

    @Override
    protected void testDelete() {

    }

    private ResponseEntity<EmploymentSessionEntity> requestPostEmploymentSessionEntity(String url, EmploymentSessionEntity entity) {
        ResponseEntity response = restTemplate.postForEntity(url, entity, EmploymentSessionEntity.class);
        LOGGER.info("POST of an EmploymentSessionEntity made at " + url);
        return response;
    }

    private void postPutAssertionsForSuccess(EmploymentSessionEntity response) {
        assertNotNull(response);
        assertNotNull(response.getEmploymentSessionId());
        assertEquals(expected.getEmployment().getEmploymentId(), response.getEmployment().getEmploymentId());
        assertEquals(expected.getStartTime(), response.getStartTime());
        assertEquals(expected.getEndTime(), expected.getEndTime());
        assertEquals(expected.getLocation().getLocationId(), response.getLocation().getLocationId());
    }

    private void runTestCreateForSuccess(String url, HttpStatus expectedStatus, EmploymentSessionEntity entity) {
        ResponseEntity response = requestPostEmploymentSessionEntity(url, entity);

        assertEquals(expectedStatus, response.getStatusCode());
        postPutAssertionsForSuccess((EmploymentSessionEntity) response.getBody());
    }

    private void runTestCreateForFailure(String url, HttpStatus expectedStatus, EmploymentSessionEntity entity) {
        ResponseEntity response = requestPostEmploymentSessionEntity(url, entity);

        assertEquals(expectedStatus, response.getStatusCode());
    }
}
