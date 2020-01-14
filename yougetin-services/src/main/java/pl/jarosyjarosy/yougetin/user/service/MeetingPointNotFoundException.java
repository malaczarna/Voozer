package pl.jarosyjarosy.yougetin.user.service;

public class MeetingPointNotFoundException extends RuntimeException {
    public MeetingPointNotFoundException(String message) {
        super(message);
    }
}
