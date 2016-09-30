package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.CredentialException;

@Service
public class ProrataUserServiceImpl extends BaseServiceImpl<ProrataUserEntity, ProrataUserRepository> implements ProrataUserService {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private EmploymentRepository employmentRepository;
    @Autowired
    private UserContactRepository userContactRepository;

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
    public ProrataUserEntity update(ProrataUserEntity user) throws CredentialException {
        String stateMessage = null;

        if (user != null && user.getEmail() != null && user.getPassword() != null) {
            ProrataUserEntity checkedUser = checkCredentialsForProrataUserEntity(user.getEmail(), user.getPassword());

            if (checkedUser != null) {
                user.setProrataUserId(checkedUser.getProrataUserId());

                // Update the user.
                ProrataUserEntity persistedUser = repository.save(user);

                // Persist all collections included with this user.
                persistCollections(user, persistedUser);

                return persistedUser;
            } else {
                stateMessage = "Incorrect username or pasword.";
                IllegalArgumentException e = new IllegalArgumentException(stateMessage);
                LOGGER.error(stateMessage, e);
                throw e;
            }

        } else {
            stateMessage = "update user failed: Neither email nor password can be null";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    public void delete(String email, String password) throws CredentialException {
        final ProrataUserEntity entityToBeDeleted = checkCredentialsForProrataUserEntity(email, password);
        if (entityToBeDeleted != null) {
            super.delete(entityToBeDeleted);
        } else {
            throw new CredentialException("Unable to find user to be deleted using the supplied credentials");
        }
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
    
    private void persistCollections(ProrataUserEntity user, ProrataUserEntity persistedUser) {
        String stateMessage = null;

        if (user != null && persistedUser.getProrataUserId() != null) {
            if (user.getListOfAccount() != null) {
                // TODO no need to persist the bank- this should be provided as
                // a list in the client rather than letting users create them.
                for (AccountEntity account : user.getListOfAccount()) {
                    account.setBank(bankRepository.findOne(1));
                    account.setProrataUser(persistedUser);
                    accountRepository.save(account);
                    LOGGER.info("Added the following account to user with ID " + user.getProrataUserId() + ": "
                            + account.toString());
                }
            }
            if (user.getListOfEmployment() != null) {
                for (EmploymentEntity employment : user.getListOfEmployment()) {
                    EmployerEntity employer = null;

                    if (employment.getEmployer() != null) {
                        employer = employment.getEmployer();
                    } else {
                        employer = new EmployerEntity();
                        employer.setName("Undeclaired Employer");
                        employment.setEmployer(employer);
                        employerRepository.save(employer);
                        LOGGER.info("Created employer for employent " + employment.toString());
                    }

                    employment.setProrataUser(persistedUser);
                    employmentRepository.save(employment);
                    LOGGER.info("Added the following account to user with ID " + user.getProrataUserId() + ": "
                            + employment.toString());
                }
            }
            if (user.getListOfSubscription() != null) {
                for (SubscriptionEntity subscription : user.getListOfSubscription()) {
                    if (subscription.getSubscriptionType() != null) {
                        subscription.setProrataUser(persistedUser);
                        subscriptionRepository.save(subscription);
                        LOGGER.info("Added the following subscription to user with ID " + user.getProrataUserId() + ": "
                                + subscription.toString());
                    }
                }
            }
            if (user.getListOfUserContact() != null) {
                for (UserContactEntity contact : user.getListOfUserContact()) {
                    contact.setProrataUser(persistedUser);
                    userContactRepository.save(contact);
                    LOGGER.info("Added the following contact to user with ID " + user.getProrataUserId() + ": "
                            + contact.toString());
                }
            }
        } else if (persistedUser.getProrataUserId() == null) {
            stateMessage = "Persistence of AccountEntities failed: Suppled perisisted ProrataUserEntity ID must not be null.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        } else {
            stateMessage = "Persistence of AccountEntities failed: Suppled ProrataUserEntity must not be null.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
    }


    private ProrataUserEntity initializeCollections(ProrataUserEntity user) {
        Hibernate.initialize(user.getListOfSubscription());
        Hibernate.initialize(user.getListOfAccount());
        Hibernate.initialize(user.getListOfEmployment());
        Hibernate.initialize(user.getListOfUserContact());

        if (user.getListOfEmployment() != null) {
            for (EmploymentEntity employment : user.getListOfEmployment()) {
                Hibernate.initialize(employment.getListOfEmploymentSession());
                Hibernate.initialize(employment.getListOfContract());
                Hibernate.initialize(employment.getListOfPayment());
                Hibernate.initialize(employment.getEmployer());
            }
        }

        return user;
    }

}
