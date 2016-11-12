package com.pawsey.api.component;

import com.pawsey.prorata.api.component.ProrataUserComponent;
import com.pawsey.prorata.api.component.impl.ProrataUserComponentImpl;
import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.repository.ProrataUserRepository;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ProrataUserComponentTest extends BaseComponentTest {

    //    @Autowired
    private ProrataUserComponent prorataUserComponent = new ProrataUserComponentImpl();

    private ProrataUserEntity expected = Mockito.mock(ProrataUserEntity.class);
    private ProrataUserRepository prorataUserRepository = Mockito.mock(ProrataUserRepository.class);

    private final String HAPPY_PATH_EMAIL = "bob@test.com";
    private final String HAPPY_PATH_PASSWORD = "password";
    private final String SAD_PATH_EMAIL = "bad@test.com";
    private final String SAD_PATH_PASSWORD = "bad";

    @Override
    @Before
    public void setup() {
        setupEntities();
        setupRepositories();
    }

    @Test
    public void testCheckCredentials() throws IncorrectPasswordException, ProrataUserNotFoundException {
        ProrataUserEntity actual = prorataUserComponent.checkCredentials(HAPPY_PATH_EMAIL, HAPPY_PATH_PASSWORD);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testCheckCredentialsShouldFailWithWrongEmail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        runCheckCredentialsForFailure(SAD_PATH_EMAIL, HAPPY_PATH_PASSWORD);
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testCheckCredentialsShouldFailWithNoEmail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        runCheckCredentialsForFailure(null, HAPPY_PATH_PASSWORD);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testCheckCredentialsShouldFailWithWrongPassword() throws IncorrectPasswordException, ProrataUserNotFoundException {
        runCheckCredentialsForFailure(HAPPY_PATH_EMAIL, SAD_PATH_PASSWORD);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testCheckCredentialsShouldFailWithNoPassword() throws IncorrectPasswordException, ProrataUserNotFoundException {
        runCheckCredentialsForFailure(HAPPY_PATH_EMAIL, null);
    }

    private void runCheckCredentialsForFailure(String email, String password) throws ProrataUserNotFoundException, IncorrectPasswordException {
        assertNull(prorataUserComponent.checkCredentials(email, password));
    }

    private void setupEntities() {
        when(expected.getProrataUserId()).thenReturn(1);
        when(expected.getEmail()).thenReturn(HAPPY_PATH_EMAIL);
        when(expected.getPassword()).thenReturn(HAPPY_PATH_PASSWORD);
    }

    private void setupRepositories() {
        when(prorataUserRepository.findByEmail(HAPPY_PATH_EMAIL)).thenReturn(expected);
        ReflectionTestUtils.setField(prorataUserComponent, "prorataUserRepository", prorataUserRepository);
    }

}
