package com.pawsey.prorata.service;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.model.TaxRuleEntity;
import org.springframework.transaction.annotation.Transactional;

public class TaxCalculatorServiceImpl extends BaseServiceImpl<TaxRuleEntity, TaxRuleRepository> implements TaxCalculatorService {

    @Override
    @Transactional
    public Double getTax() {
        return null;
    }

    @Override
    @Transactional
    public Double getTaxedIncome() {
        return null;
    }

}
