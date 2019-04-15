package pl.jarosyjarosy.yougetin.auth.service;

import javax.servlet.ServletException;

public class InvalidTokenException extends ServletException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
