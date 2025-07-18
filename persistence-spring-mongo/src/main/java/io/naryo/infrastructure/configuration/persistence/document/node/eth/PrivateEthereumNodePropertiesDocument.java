package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("private_ethereum_node")
@Getter
@Setter
public final class PrivateEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PrivateEthereumNodeDescriptor {

    private @NotBlank String groupId;
    private @NotBlank String precompiledAddress;
}
