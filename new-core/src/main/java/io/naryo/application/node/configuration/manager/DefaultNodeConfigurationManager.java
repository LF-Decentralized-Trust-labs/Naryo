package io.naryo.application.node.configuration.manager;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
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

public final class DefaultNodeConfigurationManager
        extends BaseCollectionConfigurationManager<Node, NodeDescriptor, String>
        implements NodeConfigurationManager {

    public DefaultNodeConfigurationManager(
            List<? extends CollectionSourceProvider<NodeDescriptor>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<NodeDescriptor, ?, Map<String, NodeDescriptor>> getCollector() {
        return Collectors.toMap(
                NodeDescriptor::getName,
                Function.identity(),
                NodeDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected Node map(NodeDescriptor source) {
        var common = buildCommon(source);
        return switch (source.getType()) {
            case HEDERA ->
                    new HederaNode(
                            common.id,
                            common.name,
                            common.subscription,
                            common.interaction,
                            common.connection);
            case ETHEREUM -> {
                var ethSource = (EthereumNodeDescriptor) source;
                yield switch (ethSource.getVisibility()) {
                    case PRIVATE -> {
                        var privateEthSource = (PrivateEthereumNodeDescriptor) ethSource;
                        yield new PrivateEthereumNode(
                                common.id,
                                common.name,
                                common.subscription,
                                common.interaction,
                                common.connection,
                                new GroupId(privateEthSource.getGroupId()),
                                new PrecompiledAddress(privateEthSource.getPrecompiledAddress()));
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

    private CommonParams buildCommon(NodeDescriptor source) {
        UUID id = source.getId();
        NodeName name = new NodeName(source.getName());
        var subscription = buildSubscription(source.getSubscription());
        var interaction = buildInteraction(source.getInteraction());
        var connection = buildConnection(source.getConnection());
        return new CommonParams(id, name, subscription, interaction, connection);
    }

    private BlockSubscriptionConfiguration buildSubscription(SubscriptionDescriptor descriptor) {
        BlockSubscriptionDescriptor blockDescriptor =
                (BlockSubscriptionDescriptor)
                        descriptor; // We assume this is the correct type because there's not more
        // at this moment
        var method =
                switch (blockDescriptor.method()) {
                    case POLL ->
                            new PollBlockSubscriptionMethodConfiguration(
                                    new Interval(
                                            ((PollBlockSubscriptionDescriptor) blockDescriptor)
                                                    .getInterval()));
                    case PUBSUB -> new PubSubBlockSubscriptionMethodConfiguration();
                };
        return new BlockSubscriptionConfiguration(
                method,
                blockDescriptor.getInitialBlock(),
                new NonNegativeBlockNumber(
                        BigInteger.ZERO), // TODO: Review last block processed when EventStore works
                new NonNegativeBlockNumber(blockDescriptor.getConfirmationBlocks()),
                new NonNegativeBlockNumber(blockDescriptor.getMissingTxRetryBlocks()),
                new NonNegativeBlockNumber(blockDescriptor.getEventInvalidationBlockThreshold()),
                new NonNegativeBlockNumber(blockDescriptor.getReplayBlockOffset()),
                new NonNegativeBlockNumber(blockDescriptor.getSyncBlockLimit()));
    }

    private InteractionConfiguration buildInteraction(InteractionDescriptor cfg) {
        var props = (BlockInteractionDescriptor) cfg;
        return switch (props.getMode()) {
            case ETHEREUM_RPC -> new EthereumRpcBlockInteractionConfiguration();
            case HEDERA_MIRROR_NODE -> {
                var mirCfg = (HederaMirrorNodeBlockInteractionDescriptor) props;
                yield new HederaMirrorNodeBlockInteractionConfiguration(
                        new LimitPerRequest(mirCfg.getLimitPerRequest()),
                        new RetriesPerRequest(mirCfg.getRetriesPerRequest()));
            }
        };
    }

    private NodeConnection buildConnection(NodeConnectionDescriptor descriptor) {
        var endpoint = new ConnectionEndpoint(descriptor.getEndpoint().getUrl());
        var retry =
                new RetryConfiguration(
                        descriptor.getRetry().getTimes(), descriptor.getRetry().getBackoff());
        return switch (descriptor.getType()) {
            case HTTP -> {
                var httpDescriptor = (HttpNodeConnectionDescriptor) descriptor;
                yield new HttpNodeConnection(
                        endpoint,
                        retry,
                        new MaxIdleConnections(httpDescriptor.getMaxIdleConnections()),
                        new KeepAliveDuration(httpDescriptor.getKeepAliveDuration()),
                        new ConnectionTimeout(httpDescriptor.getConnectionTimeout()),
                        new ReadTimeout(httpDescriptor.getReadTimeout()));
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
