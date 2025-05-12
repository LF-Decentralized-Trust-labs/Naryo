package io.librevents.domain.common.connection.endpoint;

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
