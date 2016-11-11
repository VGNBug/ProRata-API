package com.pawsey.api.component;

import com.pawsey.prorata.api.component.EmploymentComponent;
import com.pawsey.prorata.model.EmployerEntity;
import com.pawsey.prorata.model.EmploymentEntity;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmploymentComponentTest extends BaseComponentTest {

    private EmploymentComponent employmentComponent;
    private EmployerEntity employer;
    private EmploymentEntity employment;

    @Before
    public void setup() {
        Date now = Calendar.getInstance().getTime();

        employmentComponent = new EmploymentComponent();

        ProrataUserEntity prorataUser = new ProrataUserEntity();
        prorataUser.setProrataUserId(1);
        prorataUser.setEmail("bob@test.com");
        prorataUser.setPassword("password");

        employer = new EmployerEntity();
        employer.setEmployerId(1);
        employer.setName("The Company");

        employment = new EmploymentEntity();
        employment.setProrataUser(prorataUser);
        employment.setHourlyRate(new BigDecimal(10.75));
        employment.setHoursPerWeek(new BigDecimal(40));
        employment.setStartDate(now);
        employment.setEmployer(employer);
    }

    @Test
    public void testNameEmploymentEntity() {
        assertEquals("Employment with " + employer.getName(), runNameEmploymentEntity(employment).getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameEmploymentEntityNoEmploymentShouldThrowIllegalArgumentException() {
        assertNull(runNameEmploymentEntity(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameEmploymentEntityNoEmployerShouldThrowIllegalArgumentException() {
        employment.setEmployer(null);

        assertNull(runNameEmploymentEntity(employment));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameEmploymentEntityEmploymentWithNullNameShouldThrowIllegalArgumentException() {
        employer.setName(null);
        employment.setEmployer(employer);
        assertNull(runNameEmploymentEntity(employment));
    }

    private EmploymentEntity runNameEmploymentEntity(EmploymentEntity employment) {
        return employmentComponent.nameEmploymentEntity(employment);
    }
}

