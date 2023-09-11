package com.example.petproject.exception;

public class ObjectFieldWrongValueException extends RuntimeException{

    public ObjectFieldWrongValueException() {
    }

    public ObjectFieldWrongValueException(String message) {
        super(message);
    }
}
