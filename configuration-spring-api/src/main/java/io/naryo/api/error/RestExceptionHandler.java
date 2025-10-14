package io.naryo.api.error;

import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.queue.QueueClosedException;
import io.naryo.application.configuration.revision.queue.QueueOverflowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        BindException.class,
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception ex, HttpServletRequest req) {
        String msg =
                switch (ex) {
                    case MethodArgumentNotValidException manv ->
                            manv.getBindingResult().getFieldErrors().stream()
                                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                                    .reduce((a, b) -> a + "; " + b)
                                    .orElse("Validation failed");
                    case BindException be ->
                            be.getBindingResult().getFieldErrors().stream()
                                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                                    .reduce((a, b) -> a + "; " + b)
                                    .orElse("Validation failed");
                    case ConstraintViolationException cve ->
                            cve.getConstraintViolations().stream()
                                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                    .reduce((a, b) -> a + "; " + b)
                                    .orElse("Validation failed");
                    case HttpMessageNotReadableException nre ->
                        "Malformed JSON: " + nre.getMostSpecificCause().getMessage();
                    default -> "Validation failed";
                };
        return ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "Bad Request", msg, req.getRequestURI());
    }

    @ExceptionHandler(RevisionConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRevisionConflict(
            RevisionConflictException ex, HttpServletRequest req) {
        return ErrorResponse.of(
                HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(QueueOverflowException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleQueueOverflow(QueueOverflowException ex, HttpServletRequest req) {
        return ErrorResponse.of(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                ex.getMessage(),
                req.getRequestURI());
    }

    @ExceptionHandler(QueueClosedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleQueueClosed(QueueClosedException ex, HttpServletRequest req) {
        return ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                "Operation queue is closed",
                req.getRequestURI());
    }

    @ExceptionHandler({ Exception.class, ValidationException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknown(Exception ex, HttpServletRequest req) {
        return ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
            "Unexpected error",
                req.getRequestURI());
    }
}
