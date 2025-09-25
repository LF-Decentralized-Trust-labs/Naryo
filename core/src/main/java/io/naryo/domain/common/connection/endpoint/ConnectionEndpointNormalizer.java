package io.naryo.domain.common.connection.endpoint;

import java.net.IDN;
import java.util.*;
import java.util.regex.Pattern;

import io.naryo.domain.normalization.Normalizer;

public final class ConnectionEndpointNormalizer implements Normalizer<ConnectionEndpoint> {

    public static final ConnectionEndpointNormalizer INSTANCE = new ConnectionEndpointNormalizer();
    private static final Pattern MULTI_SLASH = Pattern.compile("/{2,}");

    @Override
    public ConnectionEndpoint normalize(ConnectionEndpoint in) {
        if (in == null) return null;

        final Protocol protocol = in.getProtocol();

        String host = in.getHost().trim().toLowerCase(Locale.ROOT);
        if (host.endsWith(".")) host = host.substring(0, host.length() - 1);

        if (!host.chars().allMatch(c -> c < 128)) {
            host = IDN.toASCII(host, IDN.ALLOW_UNASSIGNED);
        }

        final int port = in.getPort();

        String path = normalizePathForStorage(in.getPath());

        Map<String, String> headers = normalizeHeaders(in.getHeaders());

        final String url = buildUrl(protocol, host, port, path);

        return new ConnectionEndpoint(url, headers);
    }

    private static Map<String, String> normalizeHeaders(Map<String, String> in) {
        if (in == null || in.isEmpty()) return Map.of();
        TreeMap<String, String> out = new TreeMap<>();
        for (var e : in.entrySet()) {
            if (e.getKey() == null) continue;
            String k = e.getKey().trim().toLowerCase(Locale.ROOT);
            String v = e.getValue() == null ? "" : e.getValue().trim();
            out.put(k, v);
        }
        return Collections.unmodifiableMap(out);
    }

    private static String normalizePathForStorage(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String p = MULTI_SLASH.matcher(raw).replaceAll("/");
        p = p.replace("/./", "/");
        Deque<String> stack = new ArrayDeque<>();
        for (String seg : p.split("/")) {
            if (seg.isEmpty() || seg.equals(".")) continue;
            if (seg.equals("..")) {
                if (!stack.isEmpty()) stack.removeLast();
            } else stack.addLast(seg);
        }
        return String.join("/", stack);
    }

    private static String buildUrl(
            Protocol protocol, String host, int port, String pathNoLeadingSlash) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol.name().toLowerCase(Locale.ROOT)).append("://").append(host);
        int defaultPort =
                switch (protocol) {
                    case HTTP, WS -> 80;
                    case HTTPS, WSS -> 443;
                };
        if (port != defaultPort) sb.append(':').append(port);
        if (pathNoLeadingSlash != null && !pathNoLeadingSlash.isEmpty()) {
            sb.append('/').append(pathNoLeadingSlash);
        }
        return sb.toString();
    }
}
