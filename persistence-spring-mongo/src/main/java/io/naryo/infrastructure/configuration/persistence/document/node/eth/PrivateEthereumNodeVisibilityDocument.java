package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("private_eth_visibility")
@Getter
public class PrivateEthereumNodeVisibilityDocument
        extends EthereumNodeVisibilityConfigurationDocument {

    private String groupId;
    private String precompiledAddress;
}
