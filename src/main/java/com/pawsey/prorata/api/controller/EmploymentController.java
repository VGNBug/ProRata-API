package com.pawsey.prorata.api.controller;

import com.pawsey.api.controller.rest.BaseRestController;
import com.pawsey.prorata.api.exception.IncorrectPasswordException;
import com.pawsey.prorata.api.exception.ProrataUserNotFoundException;
import com.pawsey.prorata.api.service.EmploymentService;
import com.pawsey.prorata.api.service.ProrataUserService;
import com.pawsey.prorata.model.EmploymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/employment")
public class EmploymentController extends BaseRestController<EmploymentEntity, EmploymentService> {

    @Autowired
    private ProrataUserService prorataUserService;

    public EmploymentController() {
        exampleEntity = new EmploymentEntity();
    }


    @RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity readAllForUser(@PathVariable String email,
                                         @PathVariable String password) {
        try {
            return new ResponseEntity<EmploymentEntity>(service.findAllByUser(prorataUserService.signIn(email, password)), null, HttpStatus.OK);
        } catch (ProrataUserNotFoundException e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_FOUND);
        } catch (IncorrectPasswordException e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.UNAUTHORIZED);
        }
    }
}
