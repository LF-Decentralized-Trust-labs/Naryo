package io.naryo.application.node.configuration.manager;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.factory.NodeConnectionFactory;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.factory.InteractionFactory;
import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.factory.BlockSubscriptionFactory;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public final class DefaultNodeConfigurationManager
        extends BaseCollectionConfigurationManager<Node, NodeDescriptor, UUID>
        implements NodeConfigurationManager {

    private final BlockSubscriptionFactory blockSubscriptionFactory;
    private final NodeConnectionFactory nodeConnectionFactory;
    private final InteractionFactory interactionFactory;

    public DefaultNodeConfigurationManager(
            List<? extends CollectionSourceProvider<NodeDescriptor>>
                    collectionConfigurationProviders,
            BlockSubscriptionFactory blockSubscriptionFactory,
            NodeConnectionFactory nodeConnectionFactory,
            InteractionFactory interactionFactory) {
        super(collectionConfigurationProviders);
        this.blockSubscriptionFactory = blockSubscriptionFactory;
        this.nodeConnectionFactory = nodeConnectionFactory;
        this.interactionFactory = interactionFactory;
    }

    @Override
    protected Collector<NodeDescriptor, ?, Map<UUID, NodeDescriptor>> getCollector() {
        return Collectors.toMap(
                NodeDescriptor::getId,
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
                                new GroupId(valueOrNull(privateEthSource.getGroupId())),
                                new PrecompiledAddress(
                                        valueOrNull(privateEthSource.getPrecompiledAddress())));
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

    private CommonParams buildCommon(NodeDescriptor descriptor) {
        UUID id = descriptor.getId();
        NodeName name = new NodeName(valueOrNull(NodeDescriptor::getName, descriptor));
        var subscription =
                buildSubscription(valueOrNull(NodeDescriptor::getSubscription, descriptor));
        var interaction = buildInteraction(valueOrNull(NodeDescriptor::getInteraction, descriptor));
        var connection = buildConnection(valueOrNull(NodeDescriptor::getConnection, descriptor));
        return new CommonParams(id, name, subscription, interaction, connection);
    }

    private BlockSubscriptionConfiguration buildSubscription(SubscriptionDescriptor descriptor) {
        BlockSubscriptionDescriptor blockDescriptor =
                (BlockSubscriptionDescriptor)
                        descriptor; // We assume this is the correct type because there's not more
        // at this moment
        return this.blockSubscriptionFactory.create(blockDescriptor);
    }

    private InteractionConfiguration buildInteraction(InteractionDescriptor cfg) {
        return this.interactionFactory.create(cfg);
    }

    private NodeConnection buildConnection(NodeConnectionDescriptor descriptor) {
        return this.nodeConnectionFactory.create(descriptor);
    }

    private record CommonParams(
            UUID id,
            NodeName name,
            BlockSubscriptionConfiguration subscription,
            InteractionConfiguration interaction,
            NodeConnection connection) {}
}
