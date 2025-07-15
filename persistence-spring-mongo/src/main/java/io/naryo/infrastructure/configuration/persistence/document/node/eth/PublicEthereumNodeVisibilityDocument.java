package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("public_eth_visibility")
@Getter
public class PublicEthereumNodeVisibilityDocument extends EthereumNodeVisibilityConfigurationDocument {}
