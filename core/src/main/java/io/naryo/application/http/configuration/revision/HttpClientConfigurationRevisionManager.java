package io.naryo.application.http.configuration.revision;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.manager.CollectionConfigurationManager;
import io.naryo.application.configuration.revision.fingerprint.RevisionFingerprinter;
import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.http.configuration.manager.HttpClientConfigurationManager;
import io.naryo.domain.common.http.HttpClient;

public class HttpClientConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<HttpClient> {

    public HttpClientConfigurationRevisionManager(
            HttpClientConfigurationManager configurationManager,
            RevisionFingerprinter<HttpClient> fingerprinter,
            LiveRegistry<HttpClient> live) {
        super(
                asCollectionManager(configurationManager),
                fingerprinter,
                HttpClientConfigurationRevisionManager::id,
                live);
    }

    private static UUID id(HttpClient c) {
        String key =
                c.maxIdleConnections()
                        + "|"
                        + c.keepAliveDuration()
                        + "|"
                        + c.connectTimeout()
                        + "|"
                        + c.readTimeout()
                        + "|"
                        + c.writeTimeout()
                        + "|"
                        + c.callTimeout()
                        + "|"
                        + c.pingInterval()
                        + "|"
                        + c.retryOnConnectionFailure();
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
    }

    private static CollectionConfigurationManager<HttpClient> asCollectionManager(
            HttpClientConfigurationManager singleManager) {
        return new CollectionConfigurationManager<>() {
            @Override
            public Collection<HttpClient> load() {
                return List.of(singleManager.load());
            }
        };
    }
}
