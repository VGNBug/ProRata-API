package com.pawsey.prorata.api.controller;

import com.pawsey.api.rest.controller.BaseRestController;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.ProrataUserEntity;
import org.omg.CORBA.Request;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class ProrataUserController extends BaseRestController<ProrataUserEntity, ProrataUserService>
{
    /**
     * GET by id is overriden for user access. to ensure that the correct 'signIn' method is used.'
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProrataUserEntity read(@PathVariable Integer id) throws EntityNotFoundException
    {
        return null;
    }

    /**
     * @param email The {@link com.pawsey.prorata.model.ProrataUserEntity#email email address} of the ProrataUserEntity
     *              to be returned.
     * @param password The {@link com.pawsey.prorata.model.ProrataUserEntity#password password} of the ProrataUserEntity
     *                 to be returned.
     * @return A single {@link com.pawsey.prorata.model.ProrataUserEntity} with a matching email address and password.
     * @throws EntityNotFoundException If no such entity can be found, or the operation cannot be completed.
     */
    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    //@formatter:off
    public ProrataUserEntity read( @PathVariable String email,
                                   @PathVariable String password
                                   //@formatter:on
    )throws EntityNotFoundException {
        return service.signIn(email, password);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public List<ProrataUserEntity> readAll() {
        return null;
    }
}
