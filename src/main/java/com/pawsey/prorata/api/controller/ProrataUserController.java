package com.pawsey.prorata.api.controller;

import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.model.ProrataUserEntity;
import com.pawsey.prorata.api.service.ProrataUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/prorataUser")
public class ProrataUserController extends BaseRestController<ProrataUserEntity, ProrataUserService> {

    public ProrataUserController() {
        exampleEntity = new ProrataUserEntity();
    }

    /**
     * Creates a new {@link ProrataUserEntity}
     *
     * @param newUser A {@link Map} of properties corresponding to those of
     *                {@link ProrataUserEntity}
     * @return A newly persisted {@link ProrataUserEntity}
     * @throws PersistenceException if the data cannot be persisted and / or returned.
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(final @RequestBody Map<String, Object> newUser) {
        return super.create(newUser);
    }

    /**
     * This corresponds to a "sign-in" endpoint, requiring a user's email and password to read data.
     *
     * @param email    The {@link ProrataUserEntity#email email address} of the ProrataUserEntity
     *                 to be returned.
     * @param password The {@link ProrataUserEntity#password password} of the ProrataUserEntity
     *                 to be returned.
     * @return A single {@link ProrataUserEntity} with a matching email address and password.
     * @throws EntityNotFoundException If no such entity can be found, or the operation cannot be completed.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity read(@PathVariable String email,
                               @PathVariable String password) {
        try {
            return new ResponseEntity<>(service.signIn(email, password), null, HttpStatus.OK);
        } catch (ProrataUserNotFoundException e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_FOUND);
        } catch (IncorrectPasswordException e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Updates an existing {@link ProrataUserEntity}.
     *
     * @param email      The email address of the user to be updated, which is used to find the entity in the database.
     * @param password   The password of the user to be updated, which must match for the update to be allowed.
     * @param updateUser A map of how the user's data will appear once updated.
     * @return The updated {@link ProrataUserEntity}.
     */
    @RequestMapping(value = "{email}/{password}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public
    @ResponseBody
    ResponseEntity<String> update(@PathVariable String email,
                                  @PathVariable String password,
                                  final @RequestBody Map<String, Object> updateUser) {
        try {
            return new ResponseEntity(service.update(mapper.convertValue(updateUser, ProrataUserEntity.class), email, password), null, HttpStatus.OK);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Allows a user to be deleted when a valid email and password are supplied.
     *
     * @param email    The {@link ProrataUserEntity#email} attribute of the user to be deleted.
     * @param password The {@link ProrataUserEntity#password} attribute of the user to be
     *                 deleted.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> delete(@PathVariable String email,
                                         @PathVariable String password) {
        try {
            service.delete(email, password);
            return new ResponseEntity<>(getJsonFormatString("User with email address \"" + email + "\" deleted."), null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.BAD_REQUEST);
        }
    }

}
