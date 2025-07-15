package io.naryo.infrastructure.configuration.provider.node.mapper;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.http.HttpConnectionConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ws.WsConnectionConfigurationPropertiesDocument;

public abstract class ConnectionPropertiesDocumentMapper {

    public static NodeConnection fromDocument(ConnectionPropertiesDocument document) {
        if (document instanceof HttpConnectionConfigurationPropertiesDocument) {
            return mapHttpConnection((HttpConnectionConfigurationPropertiesDocument) document);
        } else if (document instanceof WsConnectionConfigurationPropertiesDocument) {
            return mapWsConnection((WsConnectionConfigurationPropertiesDocument) document);
        }
        throw new IllegalArgumentException("Unsupported document type: " + document.getClass());
    }

    private static NodeConnection mapHttpConnection(HttpConnectionConfigurationPropertiesDocument document) {
        return new HttpNodeConnection(
            new ConnectionEndpoint(document.getEndpoint().getUrl()),
            new RetryConfiguration(document.getRetry().getTimes(), document.getRetry().getBackoff()),
            new MaxIdleConnections(document.getMaxIdleConnections()),
            new KeepAliveDuration(document.getKeepAliveDuration()),
            new ConnectionTimeout(document.getConnectionTimeout()),
            new ReadTimeout(document.getReadTimeout())
        );
    }

    private static NodeConnection mapWsConnection(WsConnectionConfigurationPropertiesDocument document) {
        return new WsNodeConnection(
            new ConnectionEndpoint(document.getEndpoint().getUrl()),
            new RetryConfiguration(document.getRetry().getTimes(), document.getRetry().getBackoff())
        );
    }
}
