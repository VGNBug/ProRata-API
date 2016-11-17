package com.pawsey.prorata.service;

import com.pawsey.api.service.BaseService;
import com.pawsey.prorata.model.TaxRuleEntity;
import org.springframework.transaction.annotation.Transactional;

public interface TaxCalculatorService extends BaseService<TaxRuleEntity> {
    @Transactional
    Double getTax();

    @Transactional
    Double getTaxedIncome();
}
