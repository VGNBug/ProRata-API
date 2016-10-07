package com.pawsey.prorata.api;

import com.pawsey.api.service.BaseServiceTest;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.api.service.impl.ProrataUserServiceImpl;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ProrataUserServiceTest extends BaseServiceTest<ProrataUserEntity, ProrataUserService, ProrataUserRepository> {

    private final String email = "test@test.com";
    private final String password = "password";
    private final String firstName = "Bob";
    private final String lastName = "Alison";

    private SubscriptionTypeRepository subscriptionTypeRepository = Mockito.mock(SubscriptionTypeRepository.class);
    private SubscriptionRepository subscriptionRepository =Mockito.mock(SubscriptionRepository.class);
    private AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private EmploymentRepository employmentRepository = Mockito.mock(EmploymentRepository.class);
    private EmployerRepository employerRepository = Mockito.mock(EmployerRepository.class);
    private UserContactRepository userContactRepository = Mockito.mock(UserContactRepository.class);
    private BankRepository bankRepository = Mockito.mock(BankRepository.class);

    @Override
    @Before
    public void setupRepositorySaveMock() {
        setRepository();
        setEntity();
        setService();

        when(repository.save(any(ProrataUserEntity.class))).thenReturn(entity);
        when(repository.findByEmail(anyString())).thenReturn(entity);

        SubscriptionTypeEntity subscriptionTypeEntity = new SubscriptionTypeEntity();
        subscriptionTypeEntity.setName("standard");
        subscriptionTypeEntity.setRate(new BigDecimal(0));
        subscriptionTypeEntity.setSubscriptionTypeId(1);
        List<SubscriptionTypeEntity> subscriptionTypeEntityList = new ArrayList<>();
        subscriptionTypeEntityList.add(subscriptionTypeEntity);
        when(subscriptionTypeRepository.save(any(SubscriptionTypeEntity.class))).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findOne(anyInt())).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findAll()).thenReturn(subscriptionTypeEntityList);
        when(subscriptionTypeRepository.findByName(anyString())).thenReturn(subscriptionTypeEntity);
        ReflectionTestUtils.setField(service, "subscriptionTypeRepository", subscriptionTypeRepository);

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setProrataUser(entity);
        subscriptionEntity.setStartDateTime(Calendar.getInstance().getTime());
        subscriptionEntity.setSubscriptionType(subscriptionTypeEntity);
        List<SubscriptionEntity> subscriptionEntityList = new ArrayList<>();
        subscriptionEntityList.add(subscriptionEntity);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findOne(anyInt())).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findAll()).thenReturn(subscriptionEntityList);
        ReflectionTestUtils.setField(service, "subscriptionRepository", subscriptionRepository);

        BankEntity bankEntity = new BankEntity();
        bankEntity.setName("Test Bank");
        bankEntity.setBankId(1);
        bankEntity.setEmail("bank@test.com");
        bankEntity.setAddress("1 Bank test street");
        bankEntity.setPostcode("TE1 1ET");
        bankEntity.setTelphone("01234123123");
        List<BankEntity> bankEntityList = new ArrayList<>();
        bankEntityList.add(bankEntity);
        when(bankRepository.save(any(BankEntity.class))).thenReturn(bankEntity);
        when(bankRepository.findOne(anyInt())).thenReturn(bankEntity);
        when(bankRepository.findAll()).thenReturn(bankEntityList);
        ReflectionTestUtils.setField(service, "bankRepository", bankRepository);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1);
        accountEntity.setBank(bankEntity);
        accountEntity.setAccountNumber("12341234");
        accountEntity.setSortCode("112233");
        accountEntity.setProrataUser(entity);
        List<AccountEntity> accountEntityList = new ArrayList<>();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(accountRepository.findOne(anyInt())).thenReturn(accountEntity);
        when(accountRepository.findAll()).thenReturn(accountEntityList);
        ReflectionTestUtils.setField(service, "accountRepository", accountRepository);

        EmployerEntity employerEntity = new EmployerEntity();
        employerEntity.setEmployerId(1);
        employerEntity.setOfficePostcode("TE1 1EE");
        employerEntity.setName("Test employer");
        employerEntity.setOfficeAddress("1 Test Employer Street, Teston");
        List<EmployerEntity> employerEntityList = new ArrayList<>();
        employerEntityList.add(employerEntity);
        when(employerRepository.save(any(EmployerEntity.class))).thenReturn(employerEntity);
        when(employerRepository.findOne(anyInt())).thenReturn(employerEntity);
        when(employerRepository.findAll()).thenReturn(employerEntityList);
        ReflectionTestUtils.setField(service, "employerRepository", employerRepository);

        EmploymentEntity employmentEntity = new EmploymentEntity();
        employmentEntity.setEmploymentId(1);
        employmentEntity.setEmployer(employerEntity);
        employmentEntity.setStartDate(Calendar.getInstance().getTime());
        employmentEntity.setHoursPerWeek(new BigDecimal(40));
        employmentEntity.setName("Test employment");
        employmentEntity.setHourlyRate(new BigDecimal(10));
        List<EmploymentEntity> employmentEntityList = new ArrayList<>();
        employmentEntityList.add(employmentEntity);
        when(employmentRepository.save(any(EmploymentEntity.class))).thenReturn(employmentEntity);
        when(employmentRepository.findOne(anyInt())).thenReturn(employmentEntity);
        when(employmentRepository.findAll()).thenReturn(employmentEntityList);
        ReflectionTestUtils.setField(service, "employmentRepository", employmentRepository);

        UserContactEntity userContactEntity = new UserContactEntity();
        userContactEntity.setProrataUser(entity);
        userContactEntity.setContactType("Mobile");
        userContactEntity.setContactName("Mobile");
        userContactEntity.setContactBody("07123123123");
        userContactEntity.setUserContactId(1);
        List<UserContactEntity> userContactEntityList = new ArrayList<>();
        userContactEntityList.add(userContactEntity);
        when(userContactRepository.save(any(UserContactEntity.class))).thenReturn(userContactEntity);
        when(userContactRepository.findOne(anyInt())).thenReturn(userContactEntity);
        when(userContactRepository.findAll()).thenReturn(userContactEntityList);
        ReflectionTestUtils.setField(service, "userContactRepository", userContactRepository);

        // TODO implement tests of novel service methods

        super.setup();
    }

    @Override
    @Test
    public void testCreate() {
        checkEntityAssertions(runTestCreate(entity));
    }

    @Override
    @Test
    public void testFindById() {
        checkEntityAssertions(runTestFindById(1));
    }

    @Override
    @Test
    public void testFindAll() {
        checkEntityAssertions(runTestFindAll().get(0));
    }

    @Override
    @Test
    public void testUpdate() {
        checkEntityAssertions(runTestUpdate(entity));
    }

    @Override
    @Test
    public void testDelete() {
        runTestDelete();
    }

    @Override
    protected void setEntity() {
        entity = new ProrataUserEntity();
        entity.setProrataUserId(1);
        entity.setEmail(email);
        entity.setPassword(password);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
    }

    @Override
    protected void setRepository() {
        repository = Mockito.mock(ProrataUserRepository.class);
    }

    @Override
    protected void setService() {
        service = new ProrataUserServiceImpl();
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    private void checkEntityAssertions(ProrataUserEntity prorataUserEntity) {
        assertEquals(email, prorataUserEntity.getEmail());
        assertEquals(password, prorataUserEntity.getPassword());
        assertEquals(firstName, prorataUserEntity.getFirstName());
        assertEquals(lastName, prorataUserEntity.getLastName());
    }

}