package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.*;
import org.aspectj.weaver.Iterators;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.CredentialException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

//    @Override
//    @Transactional
//    public ProrataUserEntity create(ProrataUserEntity newUser) {
//        return super.create(newUser);
//    }

    @Override
    @Transactional
    public ProrataUserEntity create(ProrataUserEntity user) {
        String stateMessage = null;

        if (user != null) {
            if (user.getEmail() != null && user.getPassword() != null) {
                // Create the user.
                ProrataUserEntity response = repository.save(user);

                if (response != null) {
                    stateMessage = "User with email " + user.getEmail() + " created successfully. State is as follows: "
                            + user.toString();
                    LOGGER.info(stateMessage);

                    // Persist all collections included with this user.
                    persistCollections(user, response);

                    // Set the user with a default subscription if they do not
                    // have one included.
                    if (user.getListOfSubscription() == null || user.getListOfSubscription().isEmpty()) {
                        List<SubscriptionEntity> subs = new ArrayList<>();
                        subs.add(setSubscription(response, 0));
                        user.setListOfSubscription(subs);
//                        this.setSubscription(response, 0);
                    }

                    return this.initializeCollections(response);
                } else {
                    stateMessage = "User with email " + user.getEmail() + " was not updated successfully.";
                    IllegalArgumentException e = new IllegalArgumentException(stateMessage);
                    LOGGER.error(e);
                    throw e;
                }
            } else if (user.getEmail() == null && user.getPassword() != null) {
                stateMessage = "update user failed: Email cannot be null.";
                IllegalArgumentException e = new IllegalArgumentException(stateMessage);
                LOGGER.error(e);
                throw e;
            } else if (user.getEmail() != null && user.getPassword() == null) {
                stateMessage = "update user failed: Password cannot be null.";
                IllegalArgumentException e = new IllegalArgumentException(stateMessage);
                LOGGER.error(e);
                throw e;
            } else {
                stateMessage = "update user failed: Neither email nor password can be null";
                IllegalArgumentException e = new IllegalArgumentException(stateMessage);
                LOGGER.error(e);
                throw e;
            }
        } else {
            stateMessage = "No user was supplied to be updated.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
    }

    private SubscriptionEntity setSubscription(ProrataUserEntity user, Integer subscriptionTypeId) {
        String stateMessage = null;
        SubscriptionEntity subscription = new SubscriptionEntity();

        if (user.getProrataUserId() != null && subscriptionTypeRepository.findOne(subscriptionTypeId) != null) {
            SubscriptionTypeEntity subType = subscriptionTypeRepository.findOne(subscriptionTypeId);
        } else if (subscriptionTypeRepository.findOne(subscriptionTypeId) == null) {
            SubscriptionTypeEntity standard = new SubscriptionTypeEntity();
            standard.setName("standard");
            standard.setRate(new BigDecimal(0));
            subscription.setSubscriptionType(subscriptionTypeRepository.save(standard));
        } else if (user.getProrataUserId() == null) {
            stateMessage = "Adding subscription to user failed: User must be persisted and must have an ID in order to add a subscription to them";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        } else {
            stateMessage = "Adding subscription to user failed due to an unknown error.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
        subscription.setProrataUser(user);
        subscription.setSubscriptionType(subscriptionTypeRepository.findOne(subscriptionTypeId));
        subscription.setStartDateTime(Calendar.getInstance().getTime());
        return this.subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public ProrataUserEntity signIn(String emailHash, String passwordHash) throws CredentialException {
        try {
            return checkCredentialsForProrataUserEntity(emailHash, passwordHash);
        } catch (EntityNotFoundException | CredentialException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public ProrataUserEntity update(ProrataUserEntity user, String email, String password) throws CredentialException {
        String stateMessage = null;

        if (user != null && email != null && password != null) {
            ProrataUserEntity checkedUser = checkCredentialsForProrataUserEntity(email, password);

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
    @Transactional
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
                response = initializeCollections(repository.findByEmail(emailHash));
                LOGGER.info("ProrataUserEntity with password " + response.getEmail() + " found");
            } catch (Exception e) {
                throw new EntityNotFoundException("No ProrataUser with email address " + emailHash + " found");
            }
            if (passwordHash.equals(response.getPassword())) {
                return response;
            } else {
                throw new CredentialException();
            }
        } else {
            throw new IllegalArgumentException("Must provide an email address and password.");
        }
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
                        employer.setName("Undeclared Employer");
                        employment.setEmployer(employer);
                    }
                    employerRepository.save(employer);
                    LOGGER.info("Persisted employer for employment " + employment.toString());
                    employment.setProrataUser(persistedUser);
                    employmentRepository.save(employment);
                    LOGGER.info("Added the following account to user with ID " + user.getProrataUserId() + ": "
                            + employment.toString());
                }
            }
            if (user.getListOfSubscription() != null) {
                for (SubscriptionEntity subscription : user.getListOfSubscription()) {
                    if (subscription.getSubscriptionType() == null) {
                        SubscriptionTypeEntity subscriptionType;
                        if (subscriptionTypeRepository.findByName("standard") == null) {
                            subscriptionType = new SubscriptionTypeEntity();
                            subscriptionType.setName("standard");
                            subscriptionType.setRate(new BigDecimal(0));
                            subscriptionTypeRepository.save(subscriptionType);
                        }
                    }
                    subscription.setProrataUser(persistedUser);
                    subscription.setSubscriptionType(subscriptionTypeRepository.findByName("standard"));
                    subscriptionRepository.save(subscription);
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

        if (user.getListOfSubscription() != null) {
            for (SubscriptionEntity sub : user.getListOfSubscription()) {
                Hibernate.initialize(sub.getSubscriptionType());
            }
        }

        return user;
    }

}
