package pl.jarosyjarosy.yougetin.auth.model;

public class EmptyIdentityException extends RuntimeException {
    public EmptyIdentityException(String message) {
        super(message);
    }
}
