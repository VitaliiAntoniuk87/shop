package com.example.petproject.exception;

public class ObjectAlreadyExistException extends Exception {

    public ObjectAlreadyExistException() {
    }

    public ObjectAlreadyExistException(String message) {
        super(message);
    }
}
