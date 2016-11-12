package com.pawsey.prorata.api.component;

import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.model.ProrataUserEntity;

public interface ProrataUserComponent {

    ProrataUserEntity checkCredentials(String email, String password) throws ProrataUserNotFoundException, IncorrectPasswordException;
}
