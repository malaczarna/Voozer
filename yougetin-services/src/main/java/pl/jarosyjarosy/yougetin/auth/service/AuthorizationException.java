package pl.jarosyjarosy.yougetin.auth.service;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}
