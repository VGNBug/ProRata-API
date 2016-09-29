package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.ProrataUserRepository;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.CredentialException;

@Service
public class ProrataUserServiceImpl extends BaseServiceImpl<ProrataUserEntity, ProrataUserRepository> implements ProrataUserService {

    @Override
    public ProrataUserEntity create(ProrataUserEntity newUser) {
        return super.create(newUser);
    }

    @Override
    public ProrataUserEntity signIn(String emailHash, String passwordHash) throws CredentialException {
        try {
            return checkCredentialsForProrataUserEntity(emailHash, passwordHash);
        } catch (EntityNotFoundException | CredentialException e) {
            throw e;
        }
    }

    @Override
    public ProrataUserEntity update(ProrataUserEntity entity) throws CredentialException {
        if (checkCredentialsForProrataUserEntity(entity.getEmail(), entity.getPassword()) != null) {
            return super.update(entity);
        } else throw new IllegalArgumentException("Entity to be updated must contain an email and password");
    }

    @Override
    public void delete(String emailHash, String passwordHash) throws CredentialException {
        final ProrataUserEntity entityToBeDeleted = checkCredentialsForProrataUserEntity(emailHash, passwordHash);
        if (entityToBeDeleted != null) {
            super.delete(entityToBeDeleted);
        } else {
            throw new CredentialException("Unable to find user to be deleted using the supplied credentials");
        }
    }

    @Override
    protected void persistCollections(ProrataUserEntity prorataUserEntity, ProrataUserEntity t1) {

    }

    @Override
    protected ProrataUserEntity initializeCollections(ProrataUserEntity prorataUserEntity) {
        return null;
    }

    @Override
    protected boolean entityIsNull(ProrataUserEntity prorataUserEntity) {
        return false;
    }

    private ProrataUserEntity checkCredentialsForProrataUserEntity(String emailHash, String passwordHash) throws EntityNotFoundException, CredentialException {
        ProrataUserEntity response = null;
        if (!emailHash.isEmpty() && !passwordHash.isEmpty()) {
            try {
                response = repository.findByEmail(emailHash);
                LOGGER.info("ProrataUserEntity with password " + response.getEmail() + " found");
            } catch (Exception e) {
                throw new EntityNotFoundException("No ProrataUser with email address " + emailHash + " found");
            }
            if (passwordHash.equals(response.getPassword())) {
                return response;
            } else {
                throw new CredentialException();
            }
        }
        return response;
    }
}
