package io.naryo.domain.node.connection;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpointNormalizer;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.naryo.domain.normalization.Normalizer;

public final class NodeConnectionNormalizer implements Normalizer<NodeConnection> {

    public static final NodeConnectionNormalizer INSTANCE = new NodeConnectionNormalizer();

    private final ConnectionEndpointNormalizer connectionEndpointNormalizer;

    private NodeConnectionNormalizer() {
        this(ConnectionEndpointNormalizer.INSTANCE);
    }

    public NodeConnectionNormalizer(ConnectionEndpointNormalizer connectionEndpointNormalizer) {
        this.connectionEndpointNormalizer = connectionEndpointNormalizer;
    }

    @Override
    public NodeConnection normalize(NodeConnection in) {
        if (in == null) {
            return null;
        }

        return switch (in) {
            case HttpNodeConnection httpIn ->
                    httpIn.toBuilder()
                            .endpoint(connectionEndpointNormalizer.normalize(httpIn.getEndpoint()))
                            .build();
            case WsNodeConnection wsIn ->
                    wsIn.toBuilder()
                            .endpoint(connectionEndpointNormalizer.normalize(wsIn.getEndpoint()))
                            .build();
            default -> throw new IllegalStateException("Unexpected value: " + in);
        };
    }
}
