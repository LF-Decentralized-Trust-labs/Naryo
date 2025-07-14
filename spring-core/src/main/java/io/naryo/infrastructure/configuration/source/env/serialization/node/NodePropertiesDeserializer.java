package io.naryo.infrastructure.configuration.source.env.serialization.node;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.ethereum.PrivateEthereumNodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.ethereum.PublicEthereumNodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.hedera.HederaNodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.EthereumRpcBlockInteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.HederaMirrorNodeBlockInteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.PollBlockSubscriptionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.PubsubBlockSubscriptionProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class NodePropertiesDeserializer extends EnvironmentDeserializer<NodeProperties> {

    @Override
    public NodeProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String name = getTextOrNull(root.get("name"));
        String typeStr = getTextOrNull(root.get("type"));
        NodeType type =
                typeStr != null && !typeStr.isBlank()
                        ? NodeType.valueOf(typeStr.toUpperCase())
                        : NodeType.ETHEREUM;

        SubscriptionDescriptor subscription = deserializeSubscription(root, codec);
        InteractionDescriptor interaction = deserializeInteraction(root, codec);
        NodeConnectionDescriptor connection =
                (NodeConnectionDescriptor)
                        safeTreeToValue(root, "connection", codec, ConnectionProperties.class);

        return switch (type) {
            case ETHEREUM -> {
                EthereumNodeVisibility visibility =
                        safeTreeToValue(root, "visibility", codec, EthereumNodeVisibility.class);
                yield switch (visibility) {
                    case PRIVATE -> {
                        String groupId = getTextOrNull(root.get("groupId"));
                        String precompiledAddress = getTextOrNull(root.get("precompiledAddress"));
                        yield new PrivateEthereumNodeProperties(
                                getUuidOrNull(id),
                                name,
                                subscription,
                                interaction,
                                connection,
                                groupId,
                                precompiledAddress);
                    }
                    case null, default ->
                            new PublicEthereumNodeProperties(
                                    getUuidOrNull(id), name, subscription, interaction, connection);
                };
            }
            case HEDERA ->
                    new HederaNodeProperties(
                            getUuidOrNull(id), name, subscription, interaction, connection);
        };
    }

    private SubscriptionDescriptor deserializeSubscription(JsonNode root, ObjectCodec codec)
            throws IOException {
        String strategyStr = getTextOrNull(root.get("strategy"));
        SubscriptionStrategy strategy =
                strategyStr != null && !strategyStr.isBlank()
                        ? SubscriptionStrategy.valueOf(strategyStr)
                        : SubscriptionStrategy.BLOCK_BASED;

        return switch (strategy) {
            case BLOCK_BASED -> {
                BigInteger initialBlock =
                        safeTreeToValue(root, "initialBlock", codec, BigInteger.class);
                BigInteger confirmationBlocks =
                        safeTreeToValue(root, "confirmationBlocks", codec, BigInteger.class);
                BigInteger missingTxRetryBlocks =
                        safeTreeToValue(root, "missingTxRetryBlocks", codec, BigInteger.class);
                BigInteger eventInvalidationBlockThreshold =
                        safeTreeToValue(
                                root, "eventInvalidationBlockThreshold", codec, BigInteger.class);
                BigInteger replayBlockOffset =
                        safeTreeToValue(root, "replayBlockOffset", codec, BigInteger.class);
                BigInteger syncBlockLimit =
                        safeTreeToValue(root, "syncBlockLimit", codec, BigInteger.class);
                BlockSubscriptionMethod method =
                        safeTreeToValue(root, "method", codec, BlockSubscriptionMethod.class);
                yield switch (method) {
                    case PUBSUB ->
                            new PubsubBlockSubscriptionProperties(
                                    initialBlock,
                                    confirmationBlocks,
                                    missingTxRetryBlocks,
                                    eventInvalidationBlockThreshold,
                                    replayBlockOffset,
                                    syncBlockLimit);
                    case null, default -> {
                        Duration interval =
                                safeTreeToValue(root, "interval", codec, Duration.class);
                        yield new PollBlockSubscriptionProperties(
                                initialBlock,
                                confirmationBlocks,
                                missingTxRetryBlocks,
                                eventInvalidationBlockThreshold,
                                replayBlockOffset,
                                syncBlockLimit,
                                interval);
                    }
                };
            }
        };
    }

    private InteractionDescriptor deserializeInteraction(JsonNode root, ObjectCodec codec)
            throws IOException {
        String strategyStr = getTextOrNull(root.get("strategy"));
        InteractionStrategy strategy =
                strategyStr != null && !strategyStr.isBlank()
                        ? InteractionStrategy.valueOf(strategyStr.toUpperCase())
                        : InteractionStrategy.BLOCK_BASED;

        return switch (strategy) {
            case BLOCK_BASED -> {
                String modeStr = getTextOrNull(root.get("mode"));
                InteractionMode mode =
                        modeStr != null && !modeStr.isBlank()
                                ? InteractionMode.valueOf(modeStr.toUpperCase())
                                : InteractionMode.ETHEREUM_RPC;
                yield switch (mode) {
                    case ETHEREUM_RPC -> new EthereumRpcBlockInteractionProperties();
                    case HEDERA_MIRROR_NODE -> {
                        Integer limitPerRequest =
                                safeTreeToValue(root, "limitPerRequest", codec, Integer.class);
                        Integer retriesPerRequest =
                                safeTreeToValue(root, "retriesPerRequest", codec, Integer.class);
                        yield new HederaMirrorNodeBlockInteractionProperties(
                                limitPerRequest, retriesPerRequest);
                    }
                };
            }
        };
    }
}
