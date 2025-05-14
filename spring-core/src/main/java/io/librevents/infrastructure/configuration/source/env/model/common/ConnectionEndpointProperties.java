package io.librevents.infrastructure.configuration.source.env.model.common;

import java.util.Map;

public record ConnectionEndpointProperties(
        String protocol, String host, int port, String path, Map<String, String> headers) {}
