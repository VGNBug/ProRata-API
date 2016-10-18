package com.pawsey.prorata.api.service;

import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.model.ProrataUserEntity;

public interface ProrataUserService extends BaseService<ProrataUserEntity> {

    ProrataUserEntity create(ProrataUserEntity user) throws Exception;

    ProrataUserEntity signIn(String emailHash, String passwordHash) throws ProrataUserNotFoundException, IncorrectPasswordException;

    void delete(String email, String password) throws ProrataUserNotFoundException, IncorrectPasswordException;

    ProrataUserEntity update(ProrataUserEntity entity, String email, String password) throws Exception;
}
