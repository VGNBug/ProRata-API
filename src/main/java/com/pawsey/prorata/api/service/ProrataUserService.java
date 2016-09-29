package com.pawsey.prorata.api.service;

import com.pawsey.api.service.BaseService;
import com.pawsey.prorata.model.ProrataUserEntity;

import javax.security.auth.login.CredentialException;

public interface ProrataUserService extends BaseService<ProrataUserEntity> {

    ProrataUserEntity signIn(String emailHash, String passwordHash) throws CredentialException;

    void delete(String emailHash, String passwordHash) throws CredentialException;
}
