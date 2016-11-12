package com.pawsey.prorata.api.service;

import com.pawsey.api.service.BaseService;
import com.pawsey.prorata.model.EmploymentEntity;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmploymentService extends BaseService<EmploymentEntity> {
    List<EmploymentEntity> findAllByUser(ProrataUserEntity prorataUserEntity);
}
