package com.pawsey.prorata.api.controller;

import com.pawsey.api.rest.controller.BaseControllerIntegrationTest;
import com.pawsey.prorata.api.ProRataApiApplication;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.*;

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

        Date now = Calendar.getInstance().getTime();
        newUser.setListOfSubscription(makeSubscriptionsList(now));
        newUser.setListOfAccount(makeAccountsList());
        newUser.setListOfUserContact(makeUserContactsList());
        newUser.setListOfEmployment(makeEmploymentsList(now));

        ProrataUserEntity response = requestPostProrataUserEntity(API_URL + CONTROLLER_PATH, newUser);

        assertNotNull(response);
        assertNotNull(response.getProrataUserId());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
        assertEquals(makeSubscription(now).toString(), response.getListOfSubscription().get(makeSubscriptionsList(now).size() - 1).toString());
        assertEquals(makeAccount(makeBank()).toString(), response.getListOfAccount().get(makeAccountsList().size() - 1).toString());
        assertEquals(makeUserContact().toString(), response.getListOfUserContact().get(makeUserContactsList().size() -1).toString());
        assertEquals(makeEmployment(makeEmployer(), now).toString(), response.getListOfEmployment().get(makeEmploymentsList(now).size() - 1).toString());
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
    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithWrongEmailRightPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(SAD_PATH_EMAIL, expected.getPassword());

        assertNull(response);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithRightEmailWrongPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(expected.getEmail(), SAD_PATH_PASSWORD);

        assertNull(response);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRead_FailsWithWrongEmailWrongPassword() {
        ProrataUserEntity response = requestGetProrataUserEntity(SAD_PATH_EMAIL, SAD_PATH_PASSWORD);
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
        ProrataUserEntity response = requestGetProrataUserEntity(updatedExpetedUser.getEmail(), updatedExpetedUser.getPassword());

        assertNotNull(response);
        assertEquals(expected.getProrataUserId(), response.getProrataUserId());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
        assertEquals(makeSubscription(now).getSubscriptionType().getName(), response.getListOfSubscription().get(0).getSubscriptionType().getName());
        assertEquals(makeAccount(makeBank()).getAccountNumber().toString(), response.getListOfAccount().get(makeAccountsList().size() - 1).getAccountNumber().toString());
//        assertEquals(makeUserContact().toString(), response.getListOfUserContact().get(makeUserContactsList().size() -1).toString());
        assertEquals(makeEmployment(makeEmployer(), now).getName(), response.getListOfEmployment().get(makeEmploymentsList(now).size() - 1).getName());
    }

    /**
     * Update- Sad paths
     */
    @Test(expected = HttpServerErrorException.class)
    public void testUpdate_MalformedUserShouldFail() {
        MalformedProrataUserEntity malformedUpdateUser = new MalformedProrataUserEntity();
        malformedUpdateUser.setBadId(-1);
        malformedUpdateUser.setBadEmail(expected.getEmail());
        malformedUpdateUser.setBadPassword("newBad");

        String url = API_URL + CONTROLLER_PATH + "/" + expected.getEmail() + "/" + expected.getPassword();

        restTemplate.put(url, malformedUpdateUser);
        ProrataUserEntity response = requestGetProrataUserEntity(malformedUpdateUser.getBadEmail(), malformedUpdateUser.getBadPassword());

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
