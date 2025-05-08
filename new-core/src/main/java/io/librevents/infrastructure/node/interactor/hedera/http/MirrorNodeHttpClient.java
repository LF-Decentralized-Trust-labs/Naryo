package io.librevents.infrastructure.node.interactor.hedera.http;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.librevents.infrastructure.node.interactor.hedera.exception.EmptyResponseException;
import io.librevents.infrastructure.node.interactor.hedera.exception.UnexpectedResponseException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Slf4j
public class MirrorNodeHttpClient {
    private final OkHttpClient http;
    private final ObjectMapper mapper;
    private final String host;
    private final Headers headers;

    public MirrorNodeHttpClient(
            OkHttpClient http, ObjectMapper mapper, String host, Map<String, String> hdrs) {
        Objects.requireNonNull(http, "http client cannot be null");
        Objects.requireNonNull(mapper, "mapper cannot be null");
        Objects.requireNonNull(host, "host cannot be null");
        if (host.isBlank()) throw new IllegalArgumentException("host cannot be blank");
        if (!host.startsWith("http"))
            throw new IllegalArgumentException("host must start with http(s)://");
        if (host.endsWith("/")) host = host.substring(0, host.length() - 1);
        if (hdrs != null) hdrs.forEach((k, v) -> Objects.requireNonNull(v, k + " cannot be null"));
        this.http = http;
        this.mapper = mapper.copy();
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.host = host;
        this.headers = Headers.of(hdrs != null ? hdrs : Map.of());
    }

    public HttpUrl url(String path) {
        Objects.requireNonNull(path, "path cannot be null");
        if (path.isBlank()) throw new IllegalArgumentException("path cannot be blank");
        return Objects.requireNonNull(HttpUrl.parse(host + path));
    }

    public <R> R get(HttpUrl url, TypeReference<R> ref)
            throws IOException, EmptyResponseException, UnexpectedResponseException {
        Request req = new Request.Builder().url(url).headers(headers).get().build();
        try (Response resp = http.newCall(req).execute()) {
            int code = resp.code();
            return switch (HttpStatus.valueOf(code)) {
                case OK, CREATED, ACCEPTED, PARTIAL_CONTENT ->
                        mapper.readerFor(ref).withoutRootName().readValue(resp.body().string());
                case NOT_FOUND -> throw new EmptyResponseException("Not found: " + url);
                case BAD_REQUEST, CONFLICT ->
                        throw new UnexpectedResponseException("Bad request: " + url);
                default -> {
                    if (code == 429) log.warn("Rate limited on {}", url);
                    throw new UnexpectedResponseException(
                            "Unexpected status " + code + " for " + url);
                }
            };
        }
    }
}
