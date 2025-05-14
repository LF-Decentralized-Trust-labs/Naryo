package io.librevents.infrastructure.broadcaster.http.producer;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.application.broadcaster.BroadcasterProducer;
import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.event.Event;
import io.librevents.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

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
    public void produce(Broadcaster broadcaster, Event event) {
        var endpoint = ((HttpBroadcasterConfiguration) broadcaster.configuration()).getEndpoint();
        var unparsedUrl = endpoint.getUrl() + "/" + broadcaster.target().getDestination().value();
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
        return type.getName().equals("http");
    }
}
