package io.librevents.domain.node.interaction.block.hedera;

import io.librevents.domain.node.interaction.block.BlockInteractionConfiguration;
import io.librevents.domain.node.interaction.block.InteractionMode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HederaMirrorNodeBlockInteractionConfiguration
        extends BlockInteractionConfiguration {

    private final LimitPerRequest limitPerRequest;
    private final RetriesPerRequest retriesPerRequest;

    public HederaMirrorNodeBlockInteractionConfiguration(
            LimitPerRequest limitPerRequest, RetriesPerRequest retriesPerRequest) {
        super(InteractionMode.HEDERA_MIRROR_NODE);
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
    }
}
