package io.naryo.infrastructure.configuration.provider.node;

import java.math.BigInteger;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.node.configuration.provider.NodeConfigurationProvider;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.http.HttpConnectionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.EthereumNodeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.priv.PrivateEthNodeVisibilityConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.BlockInteractionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.HederaMirrorNodeBlockInteractionModeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.BlockSubscriptionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.method.PollBlockSubscriptionMethodConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvNodeConfigurationProvider implements NodeConfigurationProvider {

    private final EnvironmentProperties properties;

    public EnvNodeConfigurationProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<Node> load() {
        return properties.nodes().stream().map(this::mapNode).collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 0;
    }

    private Node mapNode(NodeProperties props) {
        var common = buildCommon(props);
        return switch (props.type()) {
            case HEDERA ->
                    new HederaNode(
                            common.id,
                            common.name,
                            common.subscription,
                            common.interaction,
                            common.connection);
            case ETHEREUM -> {
                var ethConfig = (EthereumNodeConfigurationProperties) props.configuration();
                yield switch (ethConfig.visibility()) {
                    case PRIVATE -> {
                        var privateCfg =
                                (PrivateEthNodeVisibilityConfigurationProperties)
                                        ethConfig.configuration();
                        yield new PrivateEthereumNode(
                                common.id,
                                common.name,
                                common.subscription,
                                common.interaction,
                                common.connection,
                                new GroupId(privateCfg.groupId()),
                                new PrecompiledAddress(privateCfg.precompiledAddress()));
                    }
                    case PUBLIC ->
                            new PublicEthereumNode(
                                    common.id,
                                    common.name,
                                    common.subscription,
                                    common.interaction,
                                    common.connection);
                };
            }
        };
    }

    private CommonParams buildCommon(NodeProperties props) {
        UUID id = props.id();
        NodeName name = new NodeName(props.name());
        var subscription = buildSubscription(props.subscription().configuration());
        var interaction = buildInteraction(props.interaction().configuration());
        var connection = buildConnection(props.connection());
        return new CommonParams(id, name, subscription, interaction, connection);
    }

    private BlockSubscriptionConfiguration buildSubscription(
            SubscriptionConfigurationProperties cfg) {
        var props = (BlockSubscriptionConfigurationProperties) cfg;
        var method =
                switch (props.method().type()) {
                    case POLL ->
                            new PollBlockSubscriptionMethodConfiguration(
                                    new Interval(
                                            ((PollBlockSubscriptionMethodConfigurationProperties)
                                                            props.method().configuration())
                                                    .interval()));
                    case PUBSUB -> new PubSubBlockSubscriptionMethodConfiguration();
                };
        return new BlockSubscriptionConfiguration(
                method,
                props.initialBlock(),
                new NonNegativeBlockNumber(BigInteger.ZERO),
                new NonNegativeBlockNumber(props.confirmationBlocks()),
                new NonNegativeBlockNumber(props.missingTxRetryBlocks()),
                new NonNegativeBlockNumber(props.eventInvalidationBlockThreshold()),
                new NonNegativeBlockNumber(props.replayBlockOffset()),
                new NonNegativeBlockNumber(props.syncBlockLimit()));
    }

    private InteractionConfiguration buildInteraction(InteractionConfigurationProperties cfg) {
        var props = (BlockInteractionConfigurationProperties) cfg;
        return switch (props.mode()) {
            case ETHEREUM_RPC -> new EthereumRpcBlockInteractionConfiguration();
            case HEDERA_MIRROR_NODE -> {
                var mirCfg =
                        (HederaMirrorNodeBlockInteractionModeConfigurationProperties)
                                props.configuration();
                yield new HederaMirrorNodeBlockInteractionConfiguration(
                        new LimitPerRequest(mirCfg.limitPerRequest()),
                        new RetriesPerRequest(mirCfg.retriesPerRequest()));
            }
        };
    }

    private NodeConnection buildConnection(ConnectionProperties props) {
        var endpoint = new ConnectionEndpoint(props.endpoint().url());
        var retry = new RetryConfiguration(props.retry().times(), props.retry().backoff());
        return switch (props.type()) {
            case HTTP -> {
                var httpCfg = (HttpConnectionConfigurationProperties) props.configuration();
                yield new HttpNodeConnection(
                        endpoint,
                        retry,
                        new MaxIdleConnections(httpCfg.maxIdleConnections()),
                        new KeepAliveDuration(httpCfg.keepAliveDuration()),
                        new ConnectionTimeout(httpCfg.connectionTimeout()),
                        new ReadTimeout(httpCfg.readTimeout()));
            }
            case WS -> new WsNodeConnection(endpoint, retry);
        };
    }

    private record CommonParams(
            UUID id,
            NodeName name,
            BlockSubscriptionConfiguration subscription,
            InteractionConfiguration interaction,
            NodeConnection connection) {}
}
