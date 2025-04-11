package io.librevents.domain.node.connection.endpoint;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class ConnectionEndpoint {

    private final Protocol protocol;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, String> headers;

    public ConnectionEndpoint(String url) {
        this(url, Map.of());
    }

    public ConnectionEndpoint(String url, Map<String, String> headers) {
        Objects.requireNonNull(url, "URL must not be null");
        URI uri = URI.create(url);
        Objects.requireNonNull(uri, "URL is not valid");
        this.protocol = Protocol.valueOf(uri.getScheme().toUpperCase());
        this.host = uri.getHost();
        this.port = uri.getPort() != -1 ? uri.getPort() : getDefaultPort(this.protocol);
        this.path = !uri.getPath().isEmpty() ? uri.getPath().replaceFirst("^/", "") : "";
        this.headers = headers;
    }

    private int getDefaultPort(Protocol protocol) {
        return switch (protocol) {
            case HTTP, WS -> 80;
            case HTTPS, WSS -> 443;
        };
    }

    public String getUrl() {
        return protocol + "://" + host + ":" + port + "/" + path;
    }
}
