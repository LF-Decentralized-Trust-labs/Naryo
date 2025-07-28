package io.naryo.infrastructure.event.http;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import io.naryo.domain.configuration.eventstore.block.server.http.HttpBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import static io.naryo.domain.common.connection.endpoint.ConnectionEndpoint.cleanPath;

@Slf4j
public abstract class HttpBlockEventStore<E extends Event>
        implements EventStore<E, HttpBlockEventStoreConfiguration> {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    protected HttpBlockEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        Objects.requireNonNull(httpClient, "httpClient must not be null");
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(E event, HttpBlockEventStoreConfiguration configuration) {
        TargetType targetType = map(event.getEventType());
        findTarget(targetType, configuration)
                .ifPresent(
                        target -> {
                            post(
                                    makeUrl(target, configuration),
                                    configuration.getEndpoint().getHeaders(),
                                    event);
                        });
    }

    protected Optional<EventStoreTarget> findTarget(
            TargetType targetType, HttpBlockEventStoreConfiguration configuration) {
        return configuration.getTargets().stream()
                .filter(target -> target.type().equals(targetType))
                .findFirst();
    }

    protected HttpUrl makeUrl(
            EventStoreTarget target, HttpBlockEventStoreConfiguration configuration) {
        var unparsedUrl =
                configuration.getEndpoint().getUrl() + cleanPath(target.destination().value());
        var url = HttpUrl.parse(unparsedUrl);
        if (url == null) {
            throw new IllegalArgumentException("Invalid URL: " + unparsedUrl);
        }
        return url;
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

    private TargetType map(EventType type) {
        return switch (type) {
            case BLOCK -> TargetType.BLOCK;
            case TRANSACTION -> TargetType.TRANSACTION;
            case CONTRACT -> TargetType.CONTRACT_EVENT;
        };
    }
}
