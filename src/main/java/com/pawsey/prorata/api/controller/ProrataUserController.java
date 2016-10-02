package com.pawsey.prorata.api.controller;

import com.pawsey.api.rest.controller.BaseRestController;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.security.auth.login.CredentialException;
import java.util.Map;

@RestController
@RequestMapping(value = "/prorataUser")
public class ProrataUserController extends BaseRestController<ProrataUserEntity, ProrataUserService> {

    public ProrataUserController() {
        exampleEntity = new ProrataUserEntity();
    }

    /**
     * Creates a new {@link com.pawsey.prorata.model.ProrataUserEntity}
     *
     * @param newUser A {@link java.util.Map} of properties corresponding to those of
     *                {@link com.pawsey.prorata.model.ProrataUserEntity}
     * @return A newly persisted {@link com.pawsey.prorata.model.ProrataUserEntity}
     * @throws PersistenceException if the data cannot be persisted and / or returned.
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProrataUserEntity create(final @RequestBody Map<String, Object> newUser) throws PersistenceException {
        return super.create(newUser);
    }

    /**
     * This corresponds to a "sign-in" endpoint, requiring a user's email and password to read data.
     *
     * @param email    The {@link com.pawsey.prorata.model.ProrataUserEntity#email email address} of the ProrataUserEntity
     *                 to be returned.
     * @param password The {@link com.pawsey.prorata.model.ProrataUserEntity#password password} of the ProrataUserEntity
     *                 to be returned.
     * @return A single {@link com.pawsey.prorata.model.ProrataUserEntity} with a matching email address and password.
     * @throws EntityNotFoundException If no such entity can be found, or the operation cannot be completed.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProrataUserEntity read(@PathVariable String email,
                                  @PathVariable String password) throws ProrataUserNotFoundException, CredentialException {
        try {
            ProrataUserEntity response = service.signIn(email, password);
            return response;
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    /**
     * Updates an existing {@link com.pawsey.prorata.model.ProrataUserEntity}.
     *
     * @param email The email address of the user to be updated, which is used to find the entity in the database.
     * @param password The password of the user to be updated, which must match for the update to be allowed.
     * @param updateUser A map of how the user's data will appear once updated.
     * @return The updated {@link com.pawsey.prorata.model.ProrataUserEntity}.
     * @throws CredentialException if the supplied password path-variable does not match the existing user's password.
     * @throws PersistenceException if the user is unable to be updated.
     */
    @RequestMapping(value = "{email}/{password}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProrataUserEntity update(@PathVariable String email,
                                    @PathVariable String password,
                                    final @RequestBody Map<String, Object> updateUser) throws CredentialException, PersistenceException {
        return service.update(mapper.convertValue(updateUser, exampleEntity.getClass()), email, password);
    }

    /**
     * Allows a user to be deleted when a valid email and password are supplied.
     *
     * @param email    The {@link com.pawsey.prorata.model.ProrataUserEntity#email} attribute of the user to be deleted.
     * @param password The {@link com.pawsey.prorata.model.ProrataUserEntity#password} attribute of the user to be
     *                 deleted.
     * @throws EntityNotFoundException if no such user can be found
     * @throws CredentialException     if a user with that email address is found, but the wrong password has been supplied.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email,
                       @PathVariable String password
    ) throws EntityNotFoundException, CredentialException {
        service.delete(email, password);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incorrect email address")
    @ExceptionHandler(EntityNotFoundException.class)
    public void badEmailExceptionHandler() {
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Incorrect password")
    @ExceptionHandler(CredentialException.class)
    public void badPasswordExceptionHandler() {
    }
}
