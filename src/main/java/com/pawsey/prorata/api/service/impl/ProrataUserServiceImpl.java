package com.pawsey.prorata.api.service.impl;

import com.pawsey.api.service.impl.BaseServiceImpl;
import com.pawsey.prorata.api.component.ProrataUserComponent;
import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.repository.*;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ProrataUserComponent prorataUserComponent;

    @Override
    @Transactional
    public ProrataUserEntity create(ProrataUserEntity user) throws Exception {
        if (user.getEmail() == null || "".equals(user.getEmail())) {
            throw new IllegalArgumentException("User must have an email address to be created");
        }
        if (user.getPassword() == null || "".equals(user.getPassword())) {
            throw new IllegalArgumentException("User must have a password to be created");
        }

        ProrataUserEntity persistedUser = super.create(user);
        persistCollections(user, persistedUser);

        if (user.getListOfSubscription() == null || user.getListOfSubscription().isEmpty()) {
            persistedUser = setDefaultSubscription(persistedUser);
        }

        try {
            return prorataUserComponent.checkCredentials(persistedUser.getEmail(), persistedUser.getPassword());
        } catch (ProrataUserNotFoundException | IncorrectPasswordException e) {
            throw new ProrataUserNotFoundException("User creation failed.");
        }

    }

    @Override
    @Transactional
    public ProrataUserEntity update(ProrataUserEntity user, String email, String password) throws Exception {

        if (email == null || "".equals(email)) {
            LOGGER.error("Attempt made to update user without providing original email address");
            throw new ProrataUserNotFoundException("Please provide your current email address.");
        }
        if (password == null) {
            LOGGER.error("Attempt made to update user without providing original password");
            throw new IncorrectPasswordException("Please provide your current password.");
        }
        if (user == null) {
            LOGGER.error("ProrataUserEntity update details were null");
            throw new IllegalArgumentException("Please provide details of the changes you'd like to make");
        }

        LOGGER.info("Making request to update ProrataUserEntity with email \"" + email + "\" and password \"" + password);
        ProrataUserEntity returnedResponse;

        ProrataUserEntity persisted;

        try {
            persisted = repository.findByEmail(email);

            if (persisted == null) {
                LOGGER.error("Unable to find user by email due to an unknown error.");
                throw new ProrataUserNotFoundException("We're sorry, there was an error. If the error persists, please let us know.");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to find user by email due to an unknown error.");
            throw new ProrataUserNotFoundException("We're sorry, there was an error. If the error persists, please let us know.");
        }

        if (password.equals(persisted.getPassword())) {
            if (user.getProrataUserId() == null) {
                user.setProrataUserId(persisted.getProrataUserId());
            }
            if (user.getEmail() == null) {
                user.setEmail(persisted.getEmail());
            }
            if (user.getPassword() == null) {
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
            throw new IncorrectPasswordException("Incorrect password");
        }
    }

    @Override
    @Transactional
    public ProrataUserEntity signIn(String email, String password) throws
            ProrataUserNotFoundException, IncorrectPasswordException {
        LOGGER.info("Making request for ProrataUserEntity with email \"" + email + "\" and password \"" + password + "\"");
        return prorataUserComponent.checkCredentials(email, password);
    }

    @Override
    @Transactional
    public void delete(String email, String password) throws
            ProrataUserNotFoundException, IncorrectPasswordException {
        LOGGER.info("Attempting to delete ProrataUserEntity with email \"" + email + "\" and password \"" + password + "\"");
        super.delete(prorataUserComponent.checkCredentials(email, password));
    }

    @Transactional
    private void persistCollections(ProrataUserEntity user, ProrataUserEntity persistedUser) {
        String stateMessage = null;

        if (user != null && persistedUser != null && persistedUser.getProrataUserId() != null) {
            if (user.getListOfAccount() != null) {
                // TODO no need to persist the bank- this should be provided as  a list in the client rather than letting users create them.
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
        } else if (persistedUser == null || persistedUser.getProrataUserId() == null) {
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
