package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.CredentialException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ProrataUserServiceImpl extends BaseServiceImpl<ProrataUserEntity, ProrataUserRepository> implements ProrataUserService {

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private BankRepository bankRepository;

    @Override
    @Transactional
    public ProrataUserEntity create(ProrataUserEntity user) {
        ProrataUserEntity persistedUser = super.create(user);
        persistCollections(user, persistedUser);

        if (user.getListOfSubscription() == null || user.getListOfSubscription().isEmpty()) {
            persistedUser = setDefaultSubscription(persistedUser);
        }

        return checkCredentials(persistedUser.getEmail(), persistedUser.getPassword());
    }

    @Override
    @Transactional
    public ProrataUserEntity update(ProrataUserEntity user, String email, String password) throws Exception{
        try {
            ProrataUserEntity returnedResponse;

            final ProrataUserEntity persisted = repository.findByEmail(email);

            if (password.equals(persisted.getPassword())) {
                if(user.getProrataUserId() == null) {
                    user.setProrataUserId(persisted.getProrataUserId());
                }
                if(user.getEmail() == null) {
                    user.setEmail(persisted.getEmail());
                }
                if(user.getPassword() == null) {
                    user.setPassword(persisted.getPassword());
                }

                ProrataUserEntity response = super.update(user);

                if (user.getListOfSubscription() == null) {
                    response = setDefaultSubscription(response);
                }

                persistCollections(user, response);

                returnedResponse = repository.findByEmail(response.getEmail());
                initializeCollections(returnedResponse);
                return returnedResponse;
            } else {
                throw new IllegalArgumentException("Incorrect password");
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw new EntityNotFoundException("No user with email address " + email + " found.");
        }
    }

    @Override
    @Transactional
    public ProrataUserEntity signIn(String email, String password) throws CredentialException {
        String stateMessage = null;
        ProrataUserEntity response;
        if (email != null && password != null) {
            // Get the user
            response = checkCredentials(email, password);

            // Check that the user returned is not null, and report on it.
            if (response != null) {
                stateMessage = "User with the following details was recovered by " + this.getClass().getSimpleName()
                        + ": " + response.toString();
                LOGGER.info(stateMessage);
            } else {
                stateMessage = "Sign in attempt failed because the user supplied incorrect credentials: " + getUserForErrorLogging(email, password);
                CredentialException e = new CredentialException(stateMessage);
                LOGGER.error(e);
                throw e;
            }
        } else if (email != null && password == null) {
            stateMessage = "Read user failed: password cannot be null.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        } else if (email == null && password != null) {
            stateMessage = "Read user failed: email cannot be null.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        } else {
            stateMessage = "Read user failed: email and password must be provided.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
        return response;
    }

    @Override
    @Transactional
    public void delete(String email, String password) {
        super.delete(checkCredentials(email, password));
    }

    @Transactional
    private ProrataUserEntity checkCredentials(String email, String password) {
        ProrataUserEntity response = null;

        if (email != null && password != null) {
            ProrataUserEntity matchByEmail = this.repository.findByEmail(email);

            if (matchByEmail != null && password.equals(matchByEmail.getPassword())) {
                response = this.initializeCollections(matchByEmail);
            } else {
                String stateMessage = "User with email " + email + " was not found.";
                DataRetrievalFailureException e = new DataRetrievalFailureException(stateMessage);
                LOGGER.error(e);
                throw e;
            }
        }

        return response;
    }

    @Transactional
    private void persistCollections(ProrataUserEntity user, ProrataUserEntity persistedUser) {
        String stateMessage = null;

        if (user != null && persistedUser.getProrataUserId() != null) {
            if (user.getListOfAccount() != null) {
                // TODO no need to persist the bank- this should be provided as
                // a list in the client rather than letting users create them.
                for (AccountEntity account : user.getListOfAccount()) {
                    account.setBank(bankRepository.findOne(1)); // TODO lulwut... why is this hard coded?!
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
                for (UserContactEntity userContact : user.getListOfUserContact()) {
                    userContact.setProrataUser(persistedUser);
                    userContactRepository.save(userContact);
                    LOGGER.info("Added the following contact to user with ID " + user.getProrataUserId() + ": " + userContact.toString());
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

    @Transactional
    private ProrataUserEntity setDefaultSubscription(ProrataUserEntity persistedUser) {
        if (persistedUser != null) {
            SubscriptionEntity subscription = new SubscriptionEntity();
            subscription.setStartDateTime(Calendar.getInstance().getTime());
            subscription.setProrataUser(persistedUser);
            if (subscriptionTypeRepository.findByName("standard") != null) {
                subscription.setSubscriptionType(subscriptionTypeRepository.findByName("standard"));
            } else {
                SubscriptionTypeEntity type = new SubscriptionTypeEntity();
                type.setRate(new BigDecimal(0));
                type.setName("standard");
                type = subscriptionTypeRepository.save(type);
                subscription.setSubscriptionType(type);

            }
            SubscriptionEntity persistedSubscription = subscriptionRepository.save(subscription);
            List<SubscriptionEntity> subs = new ArrayList<>();
            subs.add(persistedSubscription);
            persistedUser.setListOfSubscription(subs);
            return persistedUser;
        } else throw new IllegalArgumentException("cannot set default subscription");
    }

    @Transactional
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
            for (SubscriptionEntity subscription : user.getListOfSubscription()) {
                Hibernate.initialize(subscription.getSubscriptionType());
            }
        }

        return user;
    }

    private String getUserForErrorLogging(String email, String password) {
        StringBuilder msg = new StringBuilder();

        ProrataUserEntity entity = repository.findByEmail(email);

        if (entity != null) {
            //@formatter:off
            msg.append(
                    "\nA ProrataUser was found with the email supplied by the client: "
                            + email
                            + "\nThe ID of the user is " + entity.getProrataUserId()
            );
            //@formatter:on

            if (password.equals(entity.getPassword())) {
                msg.append("\nThe ProrataUser's password matches the password supplied by the client");
            } else {
                msg.append("\nThe password supplied by the client does not matches the password associated with the user");
            }
        } else {
            msg.append("\nNo user with the email " + email + " could be found");
        }

        return msg.toString();
    }
}
