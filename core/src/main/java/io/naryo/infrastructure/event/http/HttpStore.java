package io.naryo.infrastructure.event.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.Store;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import static io.naryo.domain.HttpConstants.HTTP_TYPE;
import static io.naryo.domain.common.connection.endpoint.ConnectionEndpoint.cleanPath;

@Slf4j
public abstract class HttpStore<K, D> implements Store<HttpStoreConfiguration, K, D> {

    protected final Class<D> clazz;
    protected final OkHttpClient httpClient;
    protected final ObjectMapper objectMapper;

    protected HttpStore(Class<D> clazz, OkHttpClient httpClient, ObjectMapper objectMapper) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(httpClient, "httpClient must not be null");
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.clazz = clazz;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(HttpStoreConfiguration configuration, K key, D data) {
        post(
                makeUrl(getDestination(configuration), configuration),
                configuration.getEndpoint().getHeaders(),
                data);
    }

    @Override
    public Optional<D> get(HttpStoreConfiguration configuration, K key) {
        try {
            return Optional.of(
                    get(
                            makeUrl(getDestination(configuration), configuration)
                                    .newBuilder()
                                    .addPathSegment(String.valueOf(key))
                                    .build(),
                            configuration.getEndpoint().getHeaders(),
                            clazz));
        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON response", e);
            return Optional.empty();
        }
    }

    @Override
    public List<D> get(HttpStoreConfiguration configuration, List<K> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                log.warn("Keys list is null or empty. Returning an empty result.");
                return List.of();
            }

            String joinedKeys =
                    keys.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.joining(","));

            return getList(
                    makeUrl(getDestination(configuration), configuration)
                            .newBuilder()
                            .addQueryParameter("ids", joinedKeys)
                            .build(),
                    configuration.getEndpoint().getHeaders(),
                    new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON response", e);
            return List.of();
        }
    }

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equalsIgnoreCase(HTTP_TYPE) && clazz == this.clazz;
    }

    protected abstract Destination getDestination(HttpStoreConfiguration configuration);

    protected HttpUrl makeUrl(Destination destination, HttpStoreConfiguration configuration) {
        var unparsedUrl = configuration.getEndpoint().getUrl() + cleanPath(destination.value());
        var url = HttpUrl.parse(unparsedUrl);
        if (url == null) {
            throw new IllegalArgumentException("Invalid URL: " + unparsedUrl);
        }
        return url;
    }

    protected <T> List<T> getList(
            HttpUrl url, Map<String, String> headers, TypeReference<List<T>> typeRef)
            throws JsonProcessingException {
        return objectMapper.readValue(
                this.makeCall(
                        () ->
                                new Request.Builder()
                                        .url(url)
                                        .headers(Headers.of(headers))
                                        .get()
                                        .build()),
                typeRef);
    }

    protected <T> T get(HttpUrl url, Map<String, String> headers, Class<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(
                this.makeCall(
                        () ->
                                new Request.Builder()
                                        .url(url)
                                        .headers(Headers.of(headers))
                                        .get()
                                        .build()),
                clazz);
    }

    protected void post(HttpUrl url, Map<String, String> headers, Object body) {
        this.makeCall(
                () ->
                        new Request.Builder()
                                .url(url)
                                .headers(Headers.of(headers))
                                .post(
                                        RequestBody.create(
                                                objectMapper.writeValueAsString(body),
                                                MediaType.get("application/json; charset=utf-8")))
                                .build());
    }

    private String makeCall(Callable<Request> makeRequest) {
        try (Response response = httpClient.newCall(makeRequest.call()).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP response: " + response.code());
            }
            try (ResponseBody body = response.body()) {
                if (body != null) {
                    return body.string();
                }
            }
        } catch (IOException e) {
            log.error("HTTP communication error while sending event to HTTP event store", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending event to HTTP event store", e);
        }

        throw new RuntimeException("Unexpected error while sending event to HTTP event store");
    }
}
