package io.naryo.api.error;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp, int status, String error, String message, String path) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path);
    }
}
