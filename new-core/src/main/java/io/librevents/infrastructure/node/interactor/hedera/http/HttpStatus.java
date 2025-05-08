package io.librevents.infrastructure.node.interactor.hedera.http;

import lombok.Getter;

@Getter
public enum HttpStatus {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    PARTIAL_CONTENT(206),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public static HttpStatus valueOf(int code) {
        for (HttpStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP status code: " + code);
    }
}
