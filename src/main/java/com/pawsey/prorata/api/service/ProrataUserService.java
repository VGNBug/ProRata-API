package com.pawsey.prorata.api.service;

import com.pawsey.api.service.BaseService;
import com.pawsey.prorata.model.ProrataUserEntity;

public interface ProrataUserService extends BaseService<ProrataUserEntity> {

    ProrataUserEntity signIn(String emailHash, String passwordHash);
}
