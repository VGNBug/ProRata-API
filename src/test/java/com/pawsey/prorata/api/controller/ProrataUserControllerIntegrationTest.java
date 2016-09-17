package com.pawsey.prorata.api.controller;

import com.pawsey.prorata.api.ProRataApiApplication;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProRataApiApplication.class)
@WebAppConfiguration
public class ProrataUserControllerIntegrationTest extends BaseControllerIntegrationTest {

    private final String SAD_PATH_EMAIL = "sadPathEmail@test.com";
    private final String SAD_PATH_PASSWORD = "sadPathPassword";

    private ProrataUserEntity expected;

    @Before
    private void setup()
    {
        expected = new ProrataUserEntity();
        expected.setEmail("bob@test.com");
        expected.setPassword("password");
    }

    @Test
    private void testSignIn_SucceedsWithRightEmailRightPassword()
    {
        ProrataUserEntity response = requestProrataUserEntity(expected.getEmail(), expected.getPassword());

        assertNotNull(response);
        assertEquals(response.getEmail(), expected.getEmail());
        assertEquals(response.getPassword(), expected.getPassword());
    }

    @Test
    private void testSignIn_FailsWithWrongEmailRightPassword()
    {
        ProrataUserEntity response = requestProrataUserEntity(SAD_PATH_EMAIL, expected.getPassword());

        assertNull(response);
    }

    @Test
    private void testSignIn_FailsWithRightEmailWrongPassword()
    {
        ProrataUserEntity response = requestProrataUserEntity(expected.getEmail(), SAD_PATH_PASSWORD);

        assertNull(response);
    }

    @Test
    private void testSignIn_FailsWithWrongEmailWrongPassword()
    {
        ProrataUserEntity response = requestProrataUserEntity(SAD_PATH_EMAIL, SAD_PATH_PASSWORD);
    }

    private ProrataUserEntity requestProrataUserEntity(String email, String password)
    {
        final String signInUrl = this.API_URL + "/" + email + "/" + password;
        ProrataUserEntity response = restTemplate.getForObject(signInUrl, ProrataUserEntity.class);
        LOGGER.info("Request for a ProrataUserEntity made at " + signInUrl);
        return response;
    }
}
