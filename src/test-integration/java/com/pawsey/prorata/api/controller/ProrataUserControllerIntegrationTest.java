package com.pawsey.prorata.api.controller;

import com.pawsey.api.controller.rest.BaseControllerIntegrationTest;
import com.pawsey.prorata.api.ProRataApiApplication;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProRataApiApplication.class)
@WebAppConfiguration
@IntegrationTest("${local.server.port}")
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

        Date now = Calendar.getInstance().getTime();
        newUser.setListOfAccount(makeAccountsList());
//        newUser.setListOfUserContact(makeUserContactsList());
        newUser.setListOfEmployment(makeEmploymentsList(now));

        ResponseEntity<ProrataUserEntity> response = requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(now.toString(), response.getBody().getListOfSubscription().get(response.getBody().getListOfSubscription().size() - 1).getStartDateTime().toString());
        postPutAssertions(now, response.getBody());
    }

    /*
    POST: Sad paths
    */
    @Test(expected = HttpClientErrorException.class)
    public void testCreate_NoDataShouldFail() {
        ProrataUserEntity nullEntity = null;
        ResponseEntity<ProrataUserEntity> response = requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, nullEntity);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testCreate_MalformattedDataShouldFail() {
        MalformedProrataUserEntity malformed = makeMalformedProrataUserEntity();

        ResponseEntity<ProrataUserEntity> response = this.requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, malformed);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNull(response.getBody());
    }

    /*
     GET: Happy path
     */
    @Test
    public void testRead() {
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(expected.getEmail(), expected.getPassword());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getEmail(), expected.getEmail());
        assertEquals(response.getBody().getPassword(), expected.getPassword());
    }

    /*
    Read: Sad paths
     */
    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithWrongEmailRightPassword() {
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(SAD_PATH_EMAIL, expected.getPassword());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithRightEmailWrongPassword() {
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(expected.getEmail(), SAD_PATH_PASSWORD);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithWrongEmailWrongPassword() {
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(SAD_PATH_EMAIL, SAD_PATH_PASSWORD);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /*
    Update: Happy path
     */
    @Test
    public void testUpdate() {
        ProrataUserEntity updatedExpetedUser = expected;

        String url = API_URL + CONTROLLER_PATH + "/" + expected.getEmail() + "/" + expected.getPassword();

        updatedExpetedUser.setPassword("newPswd");
        updatedExpetedUser.setProrataUserId(1);

        Date now = Calendar.getInstance().getTime();
        updatedExpetedUser.setListOfSubscription(makeSubscriptionsList(now));
        updatedExpetedUser.setListOfAccount(makeAccountsList());
//        updatedExpetedUser.setListOfUserContact(makeUserContactsList());
        updatedExpetedUser.setListOfEmployment(makeEmploymentsList(now));

        restTemplate.put(url, updatedExpetedUser);
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(updatedExpetedUser.getEmail(), updatedExpetedUser.getPassword());

        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mon Nov 11 00:00:00 GMT 2013", response.getBody().getListOfSubscription().get(response.getBody().getListOfSubscription().size() - 1).getStartDateTime().toString());
        postPutAssertions(now, response.getBody());
    }

    /**
     * Update- Sad paths
     */
    @Test(expected = HttpClientErrorException.class)
    public void testUpdate_MalformedUserShouldFail() {
        MalformedProrataUserEntity malformedUpdateUser = new MalformedProrataUserEntity();
        malformedUpdateUser.setBadId(-1);
        malformedUpdateUser.setBadEmail(expected.getEmail());
        malformedUpdateUser.setBadPassword("newBad");

        String url = API_URL + CONTROLLER_PATH + "/" + expected.getEmail() + "/" + expected.getPassword();

        restTemplate.put(url, malformedUpdateUser);
        ResponseEntity<ProrataUserEntity> response = requestGetProrataUserEntity(malformedUpdateUser.getBadEmail(), malformedUpdateUser.getBadPassword());

        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        assertNull(response);
    }

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

    private ResponseEntity<ProrataUserEntity> requestPostProrataUserEntity(String url, ProrataUserEntity entity) {
        ResponseEntity response = restTemplate.postForEntity(url, entity, ProrataUserEntity.class);
        LOGGER.info("POST of a ProrataUserEntity made at " + url);
        return response;
    }

    private ResponseEntity<ProrataUserEntity> requestPostProrataUserEntity(String url, MalformedProrataUserEntity entity) {
        ResponseEntity response = restTemplate.postForEntity(url, entity, ProrataUserEntity.class);
        LOGGER.info("POST of a ProrataUserEntity made at " + url);
        return response;
    }

    private ResponseEntity<ProrataUserEntity> requestGetProrataUserEntity(String email, String password) {
        final String signInUrl = API_URL + CONTROLLER_PATH + email + "/" + password;
//        ProrataUserEntity response = restTemplate.getForObject(signInUrl, ProrataUserEntity.class);
        ResponseEntity response = restTemplate.getForEntity(signInUrl, ProrataUserEntity.class);
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

    private List<SubscriptionEntity> makeSubscriptionsList(Date date) {
        SubscriptionEntity sub = makeSubscription(date);

        List<SubscriptionEntity> subscriptions = new ArrayList<>();
        subscriptions.add(sub);

        return subscriptions;
    }

    private SubscriptionEntity makeSubscription(Date date) {
        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setStartDateTime(date);
        sub.setSubscriptionType(makeSubscriptionType());
        return sub;
    }

    private SubscriptionTypeEntity makeSubscriptionType() {
        SubscriptionTypeEntity type = new SubscriptionTypeEntity();
        type.setSubscriptionTypeId(1);
        type.setName("standard");
        type.setRate(new BigDecimal(0));
        return type;
    }

    private List<EmploymentEntity> makeEmploymentsList(Date date){
        EmployerEntity employer = makeEmployer();

        EmploymentEntity employment = makeEmployment(employer, date);

        List<EmploymentEntity> employments = new ArrayList<>();
        employments.add(employment);

        return employments;
    }

    private EmployerEntity makeEmployer() {
        EmployerEntity employer = new EmployerEntity();
        employer.setEmployerId(1);
        employer.setName("Test employer");
        employer.setOfficeAddress("43 Fibbers Alley, Testinghamshire");
        employer.setOfficePostcode("TE1 4RE");
        return employer;
    }

    private EmploymentEntity makeEmployment(EmployerEntity employer, Date date) {
        EmploymentEntity employment = new EmploymentEntity();
//        employment.setEmploymentId(1);
        employment.setEmployer(employer);
        employment.setHourlyRate(new BigDecimal(10));
        employment.setHoursPerWeek(new BigDecimal(40));
        employment.setName("employment with " + employer.getName());
        employment.setStartDate(date);
        return employment;
    }

    private List<AccountEntity> makeAccountsList() {
        BankEntity bank = makeBank();

        AccountEntity account = makeAccount(bank);

        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(account);

        return accounts;
    }

    private BankEntity makeBank() {
        BankEntity bank = new BankEntity();
        bank.setBankId(2);
        bank.setName("test bank");
        return bank;
    }

    private AccountEntity makeAccount(BankEntity bank) {
        AccountEntity account = new AccountEntity();
//        account.setAccountId(1);
        account.setAccountNumber("12345678");
        account.setBank(bank);
        return account;
    }

    private List<UserContactEntity> makeUserContactsList(){
        UserContactEntity contact = makeUserContact();

        List<UserContactEntity> contacts = new ArrayList<>();
        contacts.add(contact);

        return contacts;
    }

    private UserContactEntity makeUserContact() {
        UserContactEntity contact = new UserContactEntity();
        contact.setContactName("test contact");
        contact.setContactType("test");
//        contact.setUserContactId(1);
        return contact;
    }

    private void postPutAssertions(Date now, ProrataUserEntity response) {
        assertNotNull(response);
        assertNotNull(response.getProrataUserId());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
        assertTrue(response.getListOfSubscription().size() > 0);
//        assertEquals(now.toString(), response.getListOfSubscription().get(response.getListOfSubscription().size() - 1).getStartDateTime().toString());
        assertNotNull(response.getListOfSubscription());
        assertTrue(response.getListOfAccount().size() > 0);
        assertEquals(makeAccount(makeBank()).getAccountNumber().toString(), response.getListOfAccount().get(response.getListOfAccount().size() - 1).getAccountNumber());
//        assertTrue(response.getListOfUserContact().size() > 0);
//        assertEquals(makeUserContact().toString(), response.getListOfUserContact().get(response.getListOfUserContact().size() -1).toString());
        assertTrue(response.getListOfEmployment().size() > 0);
        assertEquals(makeEmployment(makeEmployer(), now).getName(), response.getListOfEmployment().get(response.getListOfEmployment().size() - 1).getName());
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
