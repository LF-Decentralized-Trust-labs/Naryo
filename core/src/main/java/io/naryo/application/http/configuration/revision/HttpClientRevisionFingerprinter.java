package io.naryo.application.http.configuration.revision;

import io.naryo.application.configuration.revision.fingerprint.DefaultRevisionFingerprinter;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.common.http.HttpClientNormalizer;

public final class HttpClientRevisionFingerprinter
        extends DefaultRevisionFingerprinter<HttpClient> {

    public HttpClientRevisionFingerprinter(HttpClientNormalizer normalizer) {
        super(normalizer);
    }
}
