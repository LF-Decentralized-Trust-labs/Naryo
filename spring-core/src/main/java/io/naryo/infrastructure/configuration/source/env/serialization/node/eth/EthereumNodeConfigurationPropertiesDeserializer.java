package io.naryo.infrastructure.configuration.source.env.serialization.node.eth;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.EthNodeVisibilityConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.EthereumNodeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.priv.PrivateEthNodeVisibilityConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.pub.PublicEthNodeVisibilityConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class EthereumNodeConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<EthereumNodeConfigurationProperties> {
    @Override
    public EthereumNodeConfigurationProperties deserialize(
            JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String visibilityStr = getTextOrNull(root.get("visibility"));
        EthereumNodeVisibility visibility =
                visibilityStr != null && !visibilityStr.isBlank()
                        ? EthereumNodeVisibility.valueOf(visibilityStr.toUpperCase())
                        : EthereumNodeVisibility.PUBLIC;
        EthNodeVisibilityConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (visibility) {
                            case PUBLIC -> PublicEthNodeVisibilityConfigurationProperties.class;
                            case PRIVATE -> PrivateEthNodeVisibilityConfigurationProperties.class;
                        });

        return new EthereumNodeConfigurationProperties(visibility, configuration);
    }
}
