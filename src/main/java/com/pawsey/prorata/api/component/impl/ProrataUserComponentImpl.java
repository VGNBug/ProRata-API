package com.pawsey.prorata.api.component.impl;

import com.pawsey.prorata.api.component.ProrataUserComponent;
import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.repository.ProrataUserRepository;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProrataUserComponentImpl extends BaseComponentImpl implements ProrataUserComponent {

    @Autowired
    private ProrataUserRepository prorataUserRepository;

    @Override
    @Transactional
    public ProrataUserEntity checkCredentials(String email, String password) throws
            ProrataUserNotFoundException, IncorrectPasswordException {

        if (email == null || "".equals(email)) {
            LOGGER.error("Unable to find a ProrataUserEntity with email \"" + email + "\"");
            throw new ProrataUserNotFoundException("Incorrect email address");
        }
        if (password == null || "".equals(password)) {
            LOGGER.error("Password provided was null.");
            throw new IncorrectPasswordException("You must provide a password.");
        }

        ProrataUserEntity matchByEmail = null;

        try {
            matchByEmail = prorataUserRepository.findByEmail(email);
        } catch (Exception e) {
            LOGGER.error("Unable to find user by email due to an unknown error.");
            throw new ProrataUserNotFoundException("We're sorry, there was an error. If the error persists, please let us know.");
        }
        if (matchByEmail != null) {
            if (password.equals(matchByEmail.getPassword())) {
                return matchByEmail;
            } else {
                String errorMessage = "Incorrect password.";
                LOGGER.error("User provided an " + errorMessage);
                throw new IncorrectPasswordException(errorMessage);
            }
        } else {
            LOGGER.error("Unable to find a ProrataUserEntity with email \"" + email + "\"");
            throw new ProrataUserNotFoundException("Incorrect email address");
        }
    }
}
