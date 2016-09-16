package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.ProrataUserRepository;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.ProrataUserEntity;

/**
 * Created by danie on 16/09/2016.
 */
public class ProrataUserServiceImpl extends BaseServiceImpl<ProrataUserEntity, ProrataUserRepository> implements ProrataUserService {

    @Override
    public ProrataUserEntity signIn(String emailHash, String passwordHash)
    {
        return null;
    }

    @Override
    protected void persistCollections(ProrataUserEntity prorataUserEntity, ProrataUserEntity t1)
    {

    }

    @Override
    protected ProrataUserEntity initializeCollections(ProrataUserEntity prorataUserEntity)
    {
        return null;
    }

    @Override
    protected boolean entityIsNull(ProrataUserEntity prorataUserEntity)
    {
        return false;
    }
}
