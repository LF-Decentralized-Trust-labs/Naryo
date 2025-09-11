package io.naryo.infrastructure.broadcaster.http.producer;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.common.Destination;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfiguration;
import io.naryo.domain.event.Event;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import static io.naryo.domain.HttpConstants.HTTP_TYPE;
import static io.naryo.domain.common.connection.endpoint.ConnectionEndpoint.cleanPath;

@Slf4j
public final class HttpBroadcasterProducer implements BroadcasterProducer {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpBroadcasterProducer(OkHttpClient httpClient, ObjectMapper objectMapper) {
        Objects.requireNonNull(httpClient, "httpClient must not be null");
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void produce(
            Broadcaster broadcaster, BroadcasterConfiguration configuration, Event event) {
        final ConnectionEndpoint endpoint =
                ((HttpBroadcasterConfiguration) configuration).getEndpoint();

        for (Destination destination : broadcaster.getTarget().getDestinations()) {
            handleSingleDestination(event, endpoint, destination);
        }
    }

    private void handleSingleDestination(
            Event event, ConnectionEndpoint endpoint, Destination destination) {
        var unparsedUrl = endpoint.getUrl() + cleanPath(destination.value());
        var url = HttpUrl.parse(unparsedUrl);
        if (url == null) {
            throw new IllegalArgumentException("Invalid URL: " + unparsedUrl);
        }
        try {
            var request =
                    new Request.Builder()
                            .url(url)
                            .headers(Headers.of(endpoint.getHeaders()))
                            .post(
                                    RequestBody.create(
                                            objectMapper.writeValueAsString(event),
                                            MediaType.get("application/json; charset=utf-8")))
                            .build();
            httpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("Error while sending event to HTTP broadcaster: {}", e.getMessage());
        }
    }

    @Override
    public boolean supports(BroadcasterType type) {
        return type.getName().equals(HTTP_TYPE);
    }
}
