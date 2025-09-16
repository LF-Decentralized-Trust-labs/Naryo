package io.naryo.application.configuration.source.model.node.interaction.factory;

import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;

import static io.naryo.application.common.util.Defaults.setDefault;

public class DefaultInteractionFactory implements InteractionFactory {

    public static final int DEFAULT_LIMIT_PER_REQUEST = 10;
    public static final int DEFAULT_RETRIES_PER_REQUEST = 3;

    @Override
    public InteractionConfiguration create(InteractionDescriptor descriptor) {
        var block = (BlockInteractionDescriptor) descriptor;
        return switch (block.getMode()) {
            case ETHEREUM_RPC -> new EthereumRpcBlockInteractionConfiguration();
            case HEDERA_MIRROR_NODE -> {
                var hedera = (HederaMirrorNodeBlockInteractionDescriptor) block;
                applyHederaDefaults(hedera);
                yield new HederaMirrorNodeBlockInteractionConfiguration(
                        new LimitPerRequest(hedera.getLimitPerRequest().orElseThrow()),
                        new RetriesPerRequest(hedera.getRetriesPerRequest().orElseThrow()));
            }
        };
    }

    private static void applyHederaDefaults(HederaMirrorNodeBlockInteractionDescriptor d) {
        setDefault(d::getLimitPerRequest, d::setLimitPerRequest, DEFAULT_LIMIT_PER_REQUEST);
        setDefault(d::getRetriesPerRequest, d::setRetriesPerRequest, DEFAULT_RETRIES_PER_REQUEST);
    }
}
