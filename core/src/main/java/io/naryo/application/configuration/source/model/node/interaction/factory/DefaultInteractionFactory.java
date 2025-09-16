package io.naryo.application.configuration.source.model.node.interaction.factory;

import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;

public class DefaultInteractionFactory implements InteractionFactory {

    public static final int DEFAULT_HEDERA_LIMIT_PER_REQUEST = 10;
    public static final int DEFAULT_HEDERA_RETRIES_PER_REQUEST = 3;

    @Override
    public InteractionConfiguration create(InteractionDescriptor descriptor) {

        return switch (descriptor) {
            case EthereumRpcBlockInteractionDescriptor ethereum -> buildEthereum();
            case HederaMirrorNodeBlockInteractionDescriptor hedera -> buildHedera(hedera);
            default -> throw new IllegalStateException("Unexpected value: " + descriptor);
        };
    }

    private static EthereumRpcBlockInteractionConfiguration buildEthereum() {
        return new EthereumRpcBlockInteractionConfiguration();
    }

    private static HederaMirrorNodeBlockInteractionConfiguration buildHedera(
            HederaMirrorNodeBlockInteractionDescriptor descriptor) {
        int limit = descriptor.getLimitPerRequest().orElse(DEFAULT_HEDERA_LIMIT_PER_REQUEST);
        int retries = descriptor.getRetriesPerRequest().orElse(DEFAULT_HEDERA_RETRIES_PER_REQUEST);
        return new HederaMirrorNodeBlockInteractionConfiguration(
                new LimitPerRequest(limit), new RetriesPerRequest(retries));
    }
}
