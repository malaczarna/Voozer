package pl.jarosyjarosy.yougetin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.jarosyjarosy.yougetin.auth.service.AuthorizationException;
import pl.jarosyjarosy.yougetin.user.service.MeetingPointNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity handleNotFoundException(HttpServletRequest request, RecordNotFoundException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
    }

    @ExceptionHandler(MeetingPointNotFoundException.class)
    public ResponseEntity handleMeetingNotFoundException(HttpServletRequest request, MeetingPointNotFoundException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleAuthorizationException(HttpServletRequest request, MeetingPointNotFoundException e) {
        logger.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
