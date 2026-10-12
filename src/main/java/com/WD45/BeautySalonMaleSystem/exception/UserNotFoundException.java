package com.WD45.BeautySalonMaleSystem.exception;


public class UserNotFoundException
        extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}