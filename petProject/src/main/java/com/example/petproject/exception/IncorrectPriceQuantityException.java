package com.example.petproject.exception;

public class IncorrectPriceQuantityException extends RuntimeException{

    public IncorrectPriceQuantityException() {
    }

    public IncorrectPriceQuantityException(String message) {
        super(message);
    }
}
