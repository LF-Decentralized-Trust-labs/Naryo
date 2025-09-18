package io.naryo.application.configuration.source.model.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface HttpClientDescriptor extends MergeableDescriptor<HttpClientDescriptor> {

    Optional<Integer> getMaxIdleConnections();

    Optional<Duration> getKeepAliveDuration();

    Optional<Duration> getConnectTimeout();

    Optional<Duration> getReadTimeout();

    Optional<Duration> getWriteTimeout();

    Optional<Duration> getCallTimeout();

    Optional<Duration> getPingInterval();

    Optional<Boolean> getRetryOnConnectionFailure();

    void setMaxIdleConnections(Integer maxIdleConnections);

    void setKeepAliveDuration(Duration keepAliveDuration);

    void setConnectTimeout(Duration connectTimeout);

    void setReadTimeout(Duration readTimeout);

    void setWriteTimeout(Duration writeTimeout);

    void setCallTimeout(Duration callTimeout);

    void setPingInterval(Duration pingInterval);

    void setRetryOnConnectionFailure(Boolean retryOnConnectionFailure);

    @Override
    default HttpClientDescriptor merge(HttpClientDescriptor other) {
        if (other == null) {
            return this;
        }
        mergeOptionals(
                this::setMaxIdleConnections,
                this.getMaxIdleConnections(),
                other.getMaxIdleConnections());
        mergeOptionals(
                this::setKeepAliveDuration,
                this.getKeepAliveDuration(),
                other.getKeepAliveDuration());
        mergeOptionals(
                this::setConnectTimeout, this.getConnectTimeout(), other.getConnectTimeout());
        mergeOptionals(this::setReadTimeout, this.getReadTimeout(), other.getReadTimeout());
        mergeOptionals(this::setWriteTimeout, this.getWriteTimeout(), other.getWriteTimeout());
        mergeOptionals(this::setCallTimeout, this.getCallTimeout(), other.getCallTimeout());
        mergeOptionals(this::setPingInterval, this.getPingInterval(), other.getPingInterval());
        mergeOptionals(
                this::setRetryOnConnectionFailure,
                this.getRetryOnConnectionFailure(),
                other.getRetryOnConnectionFailure());

        return this;
    }
}
