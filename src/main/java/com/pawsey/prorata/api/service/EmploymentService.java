package com.pawsey.prorata.api.service;

import com.pawsey.api.service.BaseService;
import com.pawsey.prorata.model.EmploymentEntity;

public interface EmploymentService extends BaseService<EmploymentEntity> {
    EmploymentEntity create(EmploymentEntity employment, String email, String password);
}
