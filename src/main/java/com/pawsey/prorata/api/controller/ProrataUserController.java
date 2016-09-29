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
     * GET by id is overriden for user access. to ensure that the correct 'signIn' method is used.'
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProrataUserEntity read(@PathVariable Integer id) throws EntityNotFoundException {
        throw new EntityNotFoundException("Please provide sign-in details");
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
    //@formatter:off
    public ProrataUserEntity read(@PathVariable String email,
                                  @PathVariable String password
                                  //@formatter:on
    ) throws ProrataUserNotFoundException, CredentialException {

        try {
            return service.signIn(email, password);
        } catch(EntityNotFoundException e) {
            throw e;
        }
//
//        if(response != null) {
//            return response;
//        } else {
//            throw new ProrataUserNotFoundException();
//        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incorrect email address")
    @ExceptionHandler(EntityNotFoundException.class)
    public void badEmailExceptionHandler() {}

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Incorrect password")
    @ExceptionHandler(CredentialException.class)
    public void badPasswordExceptionHandler() {}

    @Override
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProrataUserEntity update(@RequestBody Map<String, Object> updateUser) throws CredentialException {
        return super.update(updateUser);
    }

    /**
     * Allows a user to be deleted when a valid email and password are supplied.
     *
     * @param email    The {@link com.pawsey.prorata.model.ProrataUserEntity#email} attribute of the user to be deleted.
     * @param password The {@link com.pawsey.prorata.model.ProrataUserEntity#password} attribute of the user to be
     *                 deleted.
     * @throws EntityNotFoundException If no such user can be found.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@formatter:off
    public void delete(@PathVariable String email,
                       @PathVariable String password
                       //@formatter:on
    ) throws EntityNotFoundException, CredentialException {
        service.delete(email, password);
    }

    /**
     * <p>
     * Safeguard endpoint to prevent attempts to delete users by their
     * {@link com.pawsey.prorata.model.ProrataUserEntity#prorataUserId} attribute.
     * </p>
     * <p>
     * {@link com.pawsey.prorata.api.controller.ProrataUserController#delete(String, String)}
     * should be used instead.
     * </p>
     *
     * @param id The database ID of the entity of the type to be deleted.
     * @throws Exception An illegal access exception to illustrate that this endpoint shouldn't be used.
     */
    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void delete(@PathVariable Integer id) throws Exception {
        throw new IllegalAccessException("Deleting a user by their ID value is forbidden. " +
                "Please provide the email address and password of the user to be deleted.");
    }
}
