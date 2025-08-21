package io.naryo.infrastructure.configuration.persistence.entity.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.NodeConnectionRetryEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ws")
@NoArgsConstructor
public class WsConnectionEntity extends ConnectionEntity implements WsNodeConnectionDescriptor {

    public WsConnectionEntity(NodeConnectionRetryEntity retry, ConnectionEndpointEntity endpoint) {
        super(retry, endpoint);
    }
}
