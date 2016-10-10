package com.pawsey.prorata.api;

import com.pawsey.api.service.BaseServiceTest;
import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.api.service.impl.ProrataUserServiceImpl;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.security.auth.login.CredentialException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProrataUserServiceTest extends BaseServiceTest<ProrataUserEntity, ProrataUserService, ProrataUserRepository> {

    private final String email = "test@test.com";
    private final String password = "password";
    private final String firstName = "Bob";
    private final String lastName = "Alison";

    private SubscriptionTypeRepository subscriptionTypeRepository = Mockito.mock(SubscriptionTypeRepository.class);
    private SubscriptionRepository subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
    private AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private EmploymentRepository employmentRepository = Mockito.mock(EmploymentRepository.class);
    private EmployerRepository employerRepository = Mockito.mock(EmployerRepository.class);
    private UserContactRepository userContactRepository = Mockito.mock(UserContactRepository.class);

    private BankRepository bankRepository = Mockito.mock(BankRepository.class);
    private final SubscriptionTypeEntity subscriptionTypeEntity = Mockito.mock(SubscriptionTypeEntity.class);
    private final SubscriptionEntity subscriptionEntity = Mockito.mock(SubscriptionEntity.class);
    private final BankEntity bankEntity = Mockito.mock(BankEntity.class);
    private final AccountEntity accountEntity = Mockito.mock(AccountEntity.class);
    private final EmployerEntity employerEntity = Mockito.mock(EmployerEntity.class);
    private final EmploymentEntity employmentEntity = Mockito.mock(EmploymentEntity.class);
    private final UserContactEntity userContactEntity = Mockito.mock(UserContactEntity.class);
    private final List<SubscriptionTypeEntity> subscriptionTypeEntityList = new ArrayList<>();
    private final List<SubscriptionEntity> subscriptionEntityList = new ArrayList<>();
    private final List<BankEntity> bankEntityList = new ArrayList<>();
    private final List<AccountEntity> accountEntityList = new ArrayList<>();
    private final List<EmployerEntity> employerEntityList = new ArrayList<>();
    private final List<EmploymentEntity> employmentEntityList = new ArrayList<>();
    private final List<UserContactEntity> userContactEntityList = new ArrayList<>();

    @Override
    @Before
    public void setupRepositorySaveMock() {
        setEntities();
        setRepositories();
        setService();

        // TODO implement tests of novel service methods

        super.setup();
    }

    @Override
    @Test
    public void testCreate() throws Exception {
        checkEntityAssertions(runTestCreate(entity));
        verify(repository).save(entity);
        verify(subscriptionRepository).save(subscriptionEntity);
        verify(employmentRepository).save(employmentEntity);
        verify(accountRepository).save(accountEntity);
    }

    @Test
    public void testCreateWithoutSubscriptionCreatesSubscription() throws Exception {
        ProrataUserEntity prorataUserEntity = Mockito.mock(ProrataUserEntity.class);
        when(prorataUserEntity.getEmail()).thenReturn(email);
        when(prorataUserEntity.getPassword()).thenReturn(password);
        when(prorataUserEntity.getFirstName()).thenReturn(firstName);
        when(prorataUserEntity.getLastName()).thenReturn(lastName);

        when(prorataUserEntity.getListOfEmployment()).thenReturn(employmentEntityList);
        when(prorataUserEntity.getListOfUserContact()).thenReturn(userContactEntityList);
        when(prorataUserEntity.getListOfAccount()).thenReturn(accountEntityList);

        checkEntityAssertions(runTestCreate(prorataUserEntity));
        verify(repository).save(prorataUserEntity);
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testCreateWithoutEmailShouldFail() throws Exception {
        ProrataUserEntity failEntity = Mockito.mock(ProrataUserEntity.class);
        when(failEntity.getPassword()).thenReturn(password);
        when(failEntity.getFirstName()).thenReturn(firstName);
        when(failEntity.getLastName()).thenReturn(lastName);

        assertNull(service.create(failEntity));
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testCreateWithoutPasswordShouldFail() throws Exception {
        ProrataUserEntity failEntity = Mockito.mock(ProrataUserEntity.class);
        when(failEntity.getEmail()).thenReturn(email);
        when(failEntity.getFirstName()).thenReturn(firstName);
        when(failEntity.getLastName()).thenReturn(lastName);

        assertNull(service.create(failEntity));
    }

    @Override
    public void testFindById() {
        checkEntityAssertions(runTestFindById(1));
    }

    @Override
    @Test
    public void testFindAll() {
        checkEntityAssertions(runTestFindAll().get(0));
    }

    @Test
    public void testSignIn() throws IncorrectPasswordException, ProrataUserNotFoundException {
        checkEntityAssertions(service.signIn(entity.getEmail(), entity.getPassword()));
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testSignInNoEmailShouldFail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        assertNull(service.signIn(null, entity.getPassword()));
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testSignInWrongEmailShouldFail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        assertNull(service.signIn("bad-email@test.com", entity.getPassword()));
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testSignInNoPasswordShouldFail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        assertNull(service.signIn(entity.getEmail(), null));
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testSignInWrongPasswordShouldFail() throws IncorrectPasswordException, ProrataUserNotFoundException {
        assertNull(service.signIn(entity.getEmail(), "badPassword"));
    }

    @Override
    @Test
    public void testUpdate() {
        checkEntityAssertions(runTestUpdate(entity));
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testUpdateNoEmailShouldFail() {
        fail("Not yet implemented");
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testUpdateWrongEmailShouldFail(){
        fail("Not yet implemented");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testUpdateNoPasswordShouldFail() {
        fail("Not yet implemented");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testUpdateWrongPasswordShouldFail() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testDelete() {
        runTestDelete();
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testDeleteNoEmailShouldFail() {
        fail("Not yet implemented");
    }

    @Test(expected = ProrataUserNotFoundException.class)
    public void testDeleteWrongEmailShouldFail(){
        fail("Not yet implemented");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testDeleteNoPasswordShouldFail() {
        fail("Not yet implemented");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testDeleteWrongPasswordShouldFail() {
        fail("Not yet implemented");
    }

    @Override
    protected void setEntities() {
        entity = Mockito.mock(ProrataUserEntity.class);
        when(entity.getProrataUserId()).thenReturn(1);
        when(entity.getEmail()).thenReturn(email);
        when(entity.getPassword()).thenReturn(password);
        when(entity.getFirstName()).thenReturn(firstName);
        when(entity.getLastName()).thenReturn(lastName);
        when(entity.getListOfAccount()).thenReturn(accountEntityList);
        when(entity.getListOfEmployment()).thenReturn(employmentEntityList);
        when(entity.getListOfSubscription()).thenReturn(subscriptionEntityList);
        when(entity.getListOfUserContact()).thenReturn(userContactEntityList);

        when(subscriptionTypeEntity.getName()).thenReturn("standard");
        when(subscriptionTypeEntity.getRate()).thenReturn(new BigDecimal(0));
        when(subscriptionTypeEntity.getSubscriptionTypeId()).thenReturn(1);

        when(subscriptionEntity.getProrataUser()).thenReturn(entity);
        when(subscriptionEntity.getStartDateTime()).thenReturn(Calendar.getInstance().getTime());
        when(subscriptionEntity.getSubscriptionType()).thenReturn(subscriptionTypeEntity);

        when(bankEntity.getName()).thenReturn("Test Bank");
        when(bankEntity.getBankId()).thenReturn(1);
        when(bankEntity.getEmail()).thenReturn("bank@test.com");
        when(bankEntity.getAddress()).thenReturn("1 Bank test street");
        when(bankEntity.getPostcode()).thenReturn("TE1 1ET");
        when(bankEntity.getTelphone()).thenReturn("01234123123");

        when(accountEntity.getAccountId()).thenReturn(1);
        when(accountEntity.getBank()).thenReturn(bankEntity);
        when(accountEntity.getAccountNumber()).thenReturn("12341234");
        when(accountEntity.getSortCode()).thenReturn("112233");
        when(accountEntity.getProrataUser()).thenReturn(entity);

        when(employerEntity.getEmployerId()).thenReturn(1);
        when(employerEntity.getOfficePostcode()).thenReturn("TE1 1EE");
        when(employerEntity.getName()).thenReturn("Test employer");
        when(employerEntity.getOfficeAddress()).thenReturn("1 Test Employer Street, Teston");

        when(employmentEntity.getEmploymentId()).thenReturn(1);
        when(employmentEntity.getEmployer()).thenReturn(employerEntity);
        when(employmentEntity.getStartDate()).thenReturn(Calendar.getInstance().getTime());
        when(employmentEntity.getHoursPerWeek()).thenReturn(new BigDecimal(40));
        when(employmentEntity.getName()).thenReturn("Test employment");
        when(employmentEntity.getHourlyRate()).thenReturn(new BigDecimal(10));

        when(userContactEntity.getProrataUser()).thenReturn(entity);
        when(userContactEntity.getContactType()).thenReturn("Mobile");
        when(userContactEntity.getContactName()).thenReturn("Mobile");
        when(userContactEntity.getContactBody()).thenReturn("07123123123");
        when(userContactEntity.getUserContactId()).thenReturn(1);
    }

    @Override
    protected void setRepositories() {
        repository = Mockito.mock(ProrataUserRepository.class);
        when(repository.save(any(ProrataUserEntity.class))).thenReturn(entity);
        when(repository.findByEmail(email)).thenReturn(entity);

        subscriptionTypeEntityList.add(subscriptionTypeEntity);
        when(subscriptionTypeRepository.save(any(SubscriptionTypeEntity.class))).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findOne(anyInt())).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findAll()).thenReturn(subscriptionTypeEntityList);
        when(subscriptionTypeRepository.findByName(anyString())).thenReturn(subscriptionTypeEntity);

        subscriptionEntityList.add(subscriptionEntity);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findOne(anyInt())).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findAll()).thenReturn(subscriptionEntityList);

        bankEntityList.add(bankEntity);
        when(bankRepository.save(any(BankEntity.class))).thenReturn(bankEntity);
        when(bankRepository.findOne(anyInt())).thenReturn(bankEntity);
        when(bankRepository.findAll()).thenReturn(bankEntityList);

        accountEntityList.add(accountEntity);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(accountRepository.findOne(anyInt())).thenReturn(accountEntity);
        when(accountRepository.findAll()).thenReturn(accountEntityList);

        employerEntityList.add(employerEntity);
        when(employerRepository.save(any(EmployerEntity.class))).thenReturn(employerEntity);
        when(employerRepository.findOne(anyInt())).thenReturn(employerEntity);
        when(employerRepository.findAll()).thenReturn(employerEntityList);

        employmentEntityList.add(employmentEntity);
        when(employmentRepository.save(any(EmploymentEntity.class))).thenReturn(employmentEntity);
        when(employmentRepository.findOne(anyInt())).thenReturn(employmentEntity);
        when(employmentRepository.findAll()).thenReturn(employmentEntityList);

        userContactEntityList.add(userContactEntity);
        when(userContactRepository.save(any(UserContactEntity.class))).thenReturn(userContactEntity);
        when(userContactRepository.findOne(anyInt())).thenReturn(userContactEntity);
        when(userContactRepository.findAll()).thenReturn(userContactEntityList);
    }

    @Override
    protected void setService() {
        service = new ProrataUserServiceImpl();
        ReflectionTestUtils.setField(service, "repository", repository);

        ReflectionTestUtils.setField(service, "subscriptionTypeRepository", subscriptionTypeRepository);
        ReflectionTestUtils.setField(service, "subscriptionRepository", subscriptionRepository);
        ReflectionTestUtils.setField(service, "bankRepository", bankRepository);
        ReflectionTestUtils.setField(service, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(service, "employerRepository", employerRepository);
        ReflectionTestUtils.setField(service, "employmentRepository", employmentRepository);
        ReflectionTestUtils.setField(service, "userContactRepository", userContactRepository);
    }

    private void checkEntityAssertions(ProrataUserEntity prorataUserEntity) {
        assertEquals(email, prorataUserEntity.getEmail());
        assertEquals(password, prorataUserEntity.getPassword());
        assertEquals(firstName, prorataUserEntity.getFirstName());
        assertEquals(lastName, prorataUserEntity.getLastName());
    }

}