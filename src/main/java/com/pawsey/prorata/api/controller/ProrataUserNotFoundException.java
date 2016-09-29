package com.pawsey.prorata.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incorrect email or password")
public class ProrataUserNotFoundException extends Exception {
    private static final long serialVersionUID = 100L;
    String message = "fuff";
}
