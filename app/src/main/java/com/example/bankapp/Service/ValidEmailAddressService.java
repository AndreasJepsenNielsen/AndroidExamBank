package com.example.bankapp.Service;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidEmailAddressService {
    public boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
