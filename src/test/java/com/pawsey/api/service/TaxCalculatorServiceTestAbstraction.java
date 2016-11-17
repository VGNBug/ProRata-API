package com.pawsey.api.service;

import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.model.TaxRuleEntity;
import com.pawsey.prorata.service.TaxCalculatorService;
import com.pawsey.prorata.service.TaxRuleRepository;

abstract class TaxCalculatorServiceTestAbstraction extends BaseServiceTest<TaxRuleEntity, TaxCalculatorService, TaxRuleRepository>{


    @Override
    public void setupRepositorySaveMock() throws IncorrectPasswordException, ProrataUserNotFoundException {

    }

    @Override
    protected void setEntities() {

    }

    @Override
    protected void setRepositories() {

    }

    @Override
    protected void setComponents() throws ProrataUserNotFoundException, IncorrectPasswordException {

    }

    @Override
    protected void setService() {

    }

    @Override
    public void testCreate() throws Exception {

    }

    @Override
    public void testFindById() {

    }

    @Override
    public void testFindAll() {

    }

    @Override
    public void testUpdate() {

    }

    @Override
    public void testDelete() {

    }
}
