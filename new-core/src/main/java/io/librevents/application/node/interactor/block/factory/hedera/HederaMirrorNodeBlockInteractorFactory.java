package io.librevents.application.node.interactor.block.factory.hedera;

import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.librevents.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.librevents.infrastructure.node.interactor.hedera.HederaMirrorNodeBlockInteractor;
import okhttp3.OkHttpClient;

public final class HederaMirrorNodeBlockInteractorFactory {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduledExecutorService;

    public HederaMirrorNodeBlockInteractorFactory(
            OkHttpClient httpClient,
            ObjectMapper objectMapper,
            ScheduledExecutorService scheduledExecutorService) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public HederaMirrorNodeBlockInteractor create(Node node) {
        if (!node.getConnection().getType().equals(NodeConnectionType.HTTP)) {
            throw new RuntimeException(
                    "Unsupported connection type for Hedera Mirror Node: "
                            + node.getConnection().getType());
        }
        if (!node.getSubscriptionConfiguration()
                .getStrategy()
                .equals(SubscriptionStrategy.BLOCK_BASED)) {
            throw new RuntimeException(
                    "Unsupported subscription strategy for Hedera Mirror Node: "
                            + node.getSubscriptionConfiguration().getStrategy());
        }
        if (!((BlockSubscriptionConfiguration) node.getSubscriptionConfiguration())
                .getMethodConfiguration()
                .getMethod()
                .equals(BlockSubscriptionMethod.POLL)) {
            throw new RuntimeException(
                    "Unsupported block subscription method for Hedera Mirror Node: "
                            + ((BlockSubscriptionConfiguration) node.getSubscriptionConfiguration())
                                    .getMethodConfiguration()
                                    .getMethod());
        }
        PollBlockSubscriptionMethodConfiguration subscriptionConfig =
                (PollBlockSubscriptionMethodConfiguration)
                        ((BlockSubscriptionConfiguration) node.getSubscriptionConfiguration())
                                .getMethodConfiguration();
        ConnectionEndpoint endpoint = node.getConnection().getEndpoint();
        HederaMirrorNodeBlockInteractionConfiguration interactionConfiguration =
                (HederaMirrorNodeBlockInteractionConfiguration) node.getInteractionConfiguration();
        return new HederaMirrorNodeBlockInteractor(
                httpClient,
                objectMapper,
                endpoint.getProtocol() + "://" + endpoint.getHost() + ":" + endpoint.getPort(),
                endpoint.getHeaders(),
                subscriptionConfig.getInterval().value(),
                interactionConfiguration.getRetriesPerRequest().value(),
                interactionConfiguration.getLimitPerRequest().value(),
                scheduledExecutorService);
    }
}
