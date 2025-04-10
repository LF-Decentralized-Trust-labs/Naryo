package io.librevents.domain.node.connection.endpoint;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum Protocol {
    HTTP("http"),
    HTTPS("https"),
    WS("ws"),
    WSS("wss");

    private final String value;

    Protocol(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
