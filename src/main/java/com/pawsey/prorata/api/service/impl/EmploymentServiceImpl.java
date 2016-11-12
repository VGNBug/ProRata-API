package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.EmploymentRepository;
import com.pawsey.prorata.api.service.EmploymentService;
import com.pawsey.prorata.model.EmploymentEntity;
import org.springframework.stereotype.Service;

@Service
public class EmploymentServiceImpl extends BaseServiceImpl<EmploymentEntity, EmploymentRepository> implements EmploymentService {

    @Override
    public EmploymentEntity create(EmploymentEntity employment, String email, String password) {
        return null;
    }
}
