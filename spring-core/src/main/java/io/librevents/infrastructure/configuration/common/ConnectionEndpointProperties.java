package io.librevents.infrastructure.configuration.common;

import java.util.Map;

public record ConnectionEndpointProperties(
        String protocol, String host, int port, String path, Map<String, String> headers) {}
