package com.pawsey.prorata.api.controller;

import com.pawsey.api.rest.controller.BaseControllerIntegrationTest;
import com.pawsey.prorata.api.ProRataApiApplication;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProRataApiApplication.class)
@WebAppConfiguration
public class ProrataUserControllerIntegrationTest extends BaseControllerIntegrationTest {

    public static final String CONTROLLER_PATH = "/prorataUser/";
    private final String SAD_PATH_EMAIL = "sadPathEmail@test.com";
    private final String SAD_PATH_PASSWORD = "sadPathPassword";

    private ProrataUserEntity expected;

    @Before
    public void setup() {
        expected = new ProrataUserEntity();
        expected.setEmail("bob@test.com");
        expected.setPassword("password");
    }

    /*
    POST: Happy path
     */
    @Test
    public void testCreate() {
        ProrataUserEntity newUser = expected;
        newUser.setEmail("newUser@test.com");
        ProrataUserEntity response = requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, newUser);

        assertNotNull(response);
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
    }

    /*
    POST: Sad paths
    */
    @Test(expected = NullPointerException.class)
    public void testCreate_NoDataShouldFail() {
        ProrataUserEntity nullEntity = null;
        ProrataUserEntity response = requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, nullEntity);

        assertNull(response);
    }

    @Test(expected = HttpServerErrorException.class)
    public void testCreate_MalformattedDataShouldFail() {
        MalformedProrataUserEntity malformed = makeMalformedProrataUserEntity();

        ProrataUserEntity response = this.requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, malformed);

        assertNull(response);
    }

    /*
     GET: Happy path
     */
    @Test
    public void testRead() {
        ProrataUserEntity response = requestGetProrataUserEntity(expected.getEmail(), expected.getPassword());

        assertNotNull(response);
        assertEquals(response.getEmail(), expected.getEmail());
        assertEquals(response.getPassword(), expected.getPassword());
    }

    /*
    Read: Sad paths
     */
    @Test(expected = HttpServerErrorException.class)
    public void testRead_FailsWithWrongEmailRightPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(SAD_PATH_EMAIL, expected.getPassword());

        assertNull(response);
    }

    @Test(expected = HttpServerErrorException.class)
    public void testRead_FailsWithRightEmailWrongPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(expected.getEmail(), SAD_PATH_PASSWORD);

        assertNull(response);
    }

    @Test(expected = HttpServerErrorException.class)
    public void testRead_FailsWithWrongEmailWrongPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(SAD_PATH_EMAIL, SAD_PATH_PASSWORD);
    }

    /*
    Update: Happy path
     */
    @Test
    public void testUpdate() {
        ProrataUserEntity updatedExpetedUser = new ProrataUserEntity();
        updatedExpetedUser.setProrataUserId(expected.getProrataUserId());
        updatedExpetedUser.setEmail(expected.getEmail());
        updatedExpetedUser.setPassword("newPassword");

        String url = API_URL + CONTROLLER_PATH + expected.getEmail() + "/" + expected.getPassword();

        restTemplate.put(url, updatedExpetedUser);
        ProrataUserEntity response = requestGetProrataUserEntity(updatedExpetedUser.getEmail(), updatedExpetedUser.getPassword());

        assertNotNull(response);
        assertEquals(expected.getProrataUserId(), response.getProrataUserId());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
    }

    /**
     * Update- Sad paths
     */
    // TODO investigate why UnrecognizedPropertyExceptions are not being thrown
//    @Test(expected = UnrecognizedPropertyException.class)
//    public void testUpdate_MalformedUserShouldFail() {
//        MalformedProrataUserEntity malformedUpdateUser = new MalformedProrataUserEntity();
//        malformedUpdateUser.setBadId(-1);
//        malformedUpdateUser.setBadEmail(expected.getEmail());
//        malformedUpdateUser.setBadPassword("newBadPassword");
//
//        String url = API_URL + CONTROLLER_PATH;
//
//        restTemplate.put(url, malformedUpdateUser);
//        ProrataUserEntity response = requestGetProrataUserEntity(malformedUpdateUser.getBadEmail(), malformedUpdateUser.getBadPassword());
//
//        assertNull(response);
//    }

    /*
    Delete: Happy path
     */
    public void testDelete() {
        makeDeleteRequest(CONTROLLER_PATH + expected.getEmail() + "/" + expected.getPassword(), null);
    }

    /*
    Delete: Sad paths
     */
    @Test(expected = NullPointerException.class)
    public void testDelete_shouldFailIfSuppliedWithWrongEmail() {
        makeDeleteRequest("/user/badEmail/" + expected.getPassword(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_shouldFailIfSuppliedWithRightEmailWrongPassword() {
        makeDeleteRequest(CONTROLLER_PATH + expected.getEmail() + "/badPassword", null);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_shouldFailIfSuppliedWithNoEmailNoPassword() {
        makeDeleteRequest(CONTROLLER_PATH, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_shouldFailIfSuppliedWithIdParam() {
        makeDeleteRequest(CONTROLLER_PATH, expected.getProrataUserId());
    }

    private ProrataUserEntity requestPostProrataUserEntity(String url, ProrataUserEntity entity) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("prorata_user_id", "-1");
        vars.put("email", expected.getEmail());
        vars.put("password", entity.getPassword());

        ProrataUserEntity response = restTemplate.postForObject(url, entity, ProrataUserEntity.class, vars);
        LOGGER.info("POST of a ProrataUserEntity made at " + url);
        return response;
    }

    private ProrataUserEntity requestPostProrataUserEntity(String url, MalformedProrataUserEntity entity) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("prorata_user_id", entity.getBadId().toString());
        vars.put("email", entity.getBadEmail());
        vars.put("password", entity.getBadPassword());

        ProrataUserEntity response = restTemplate.postForObject(url, entity, ProrataUserEntity.class, vars);
        LOGGER.info("POST of a ProrataUserEntity made at " + url);
        return response;
    }

    private ProrataUserEntity requestGetProrataUserEntity(String email, String password) {
        final String signInUrl = API_URL + CONTROLLER_PATH + email + "/" + password;
        ProrataUserEntity response = restTemplate.getForObject(signInUrl, ProrataUserEntity.class);
        LOGGER.info("GET for a ProrataUserEntity made at " + signInUrl);
        return response;
    }

    private MalformedProrataUserEntity makeMalformedProrataUserEntity() {
        MalformedProrataUserEntity malformed = new MalformedProrataUserEntity();
        malformed.setBadId(1);
        malformed.setBadEmail("bob@test.com");
        malformed.setBadPassword("password");
        return malformed;
    }

    private class MalformedProrataUserEntity {

        private Integer badId;
        private String badEmail;
        private String badPassword;

        public Integer getBadId() {
            return badId;
        }

        public void setBadId(Integer badId) {
            this.badId = badId;
        }

        public String getBadPassword() {
            return badPassword;
        }

        public void setBadPassword(String badPassword) {
            this.badPassword = badPassword;
        }

        public String getBadEmail() {
            return badEmail;
        }

        public void setBadEmail(String badEmail) {
            this.badEmail = badEmail;
        }

    }

}
