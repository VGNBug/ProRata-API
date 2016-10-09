package com.pawsey.prorata.api;

import com.pawsey.api.service.BaseServiceTest;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.api.service.impl.ProrataUserServiceImpl;
import com.pawsey.prorata.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
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
    private final SubscriptionTypeEntity subscriptionTypeEntity = Mockito.mock( SubscriptionTypeEntity.class);
    private final SubscriptionEntity subscriptionEntity = Mockito.mock( SubscriptionEntity.class);
    private final BankEntity bankEntity = Mockito.mock( BankEntity.class);
    private final AccountEntity accountEntity = Mockito.mock( AccountEntity.class);
    private final EmployerEntity employerEntity = Mockito.mock( EmployerEntity.class);
    private final EmploymentEntity employmentEntity = Mockito.mock( EmploymentEntity.class);
    private final UserContactEntity userContactEntity = Mockito.mock( UserContactEntity.class);

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
    public void testCreate() {
        checkEntityAssertions(runTestCreate(entity));
        verify(subscriptionTypeRepository).save(subscriptionTypeEntity);
        verify(subscriptionRepository).save(subscriptionEntity);
        verify(accountRepository ).save(accountEntity);
        verify(employmentRepository ).save(employmentEntity);
        verify(employerRepository ).save(employerEntity);
        verify(userContactRepository ).save(userContactEntity);
        verify(bankRepository ).save(bankEntity);
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
    protected void setEntities() {
        entity = Mockito.mock(ProrataUserEntity.class);
        when(entity.getProrataUserId()).thenReturn(1);
        when(entity.getEmail()).thenReturn(email);
        when(entity.getPassword()).thenReturn(password);
        when(entity.getFirstName()).thenReturn(firstName);
        when(entity.getLastName()).thenReturn(lastName);

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
        when(repository.findByEmail(anyString())).thenReturn(entity);

        List<SubscriptionTypeEntity> subscriptionTypeEntityList = new ArrayList<>();
        subscriptionTypeEntityList.add(subscriptionTypeEntity);
        when(subscriptionTypeRepository.save(any(SubscriptionTypeEntity.class))).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findOne(anyInt())).thenReturn(subscriptionTypeEntity);
        when(subscriptionTypeRepository.findAll()).thenReturn(subscriptionTypeEntityList);
        when(subscriptionTypeRepository.findByName(anyString())).thenReturn(subscriptionTypeEntity);

        List<SubscriptionEntity> subscriptionEntityList = new ArrayList<>();
        subscriptionEntityList.add(subscriptionEntity);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findOne(anyInt())).thenReturn(subscriptionEntity);
        when(subscriptionRepository.findAll()).thenReturn(subscriptionEntityList);

        List<BankEntity> bankEntityList = new ArrayList<>();
        bankEntityList.add(bankEntity);
        when(bankRepository.save(any(BankEntity.class))).thenReturn(bankEntity);
        when(bankRepository.findOne(anyInt())).thenReturn(bankEntity);
        when(bankRepository.findAll()).thenReturn(bankEntityList);

        List<AccountEntity> accountEntityList = new ArrayList<>();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(accountRepository.findOne(anyInt())).thenReturn(accountEntity);
        when(accountRepository.findAll()).thenReturn(accountEntityList);

        List<EmployerEntity> employerEntityList = new ArrayList<>();
        employerEntityList.add(employerEntity);
        when(employerRepository.save(any(EmployerEntity.class))).thenReturn(employerEntity);
        when(employerRepository.findOne(anyInt())).thenReturn(employerEntity);
        when(employerRepository.findAll()).thenReturn(employerEntityList);

        List<EmploymentEntity> employmentEntityList = new ArrayList<>();
        employmentEntityList.add(employmentEntity);
        when(employmentRepository.save(any(EmploymentEntity.class))).thenReturn(employmentEntity);
        when(employmentRepository.findOne(anyInt())).thenReturn(employmentEntity);
        when(employmentRepository.findAll()).thenReturn(employmentEntityList);

        List<UserContactEntity> userContactEntityList = new ArrayList<>();
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