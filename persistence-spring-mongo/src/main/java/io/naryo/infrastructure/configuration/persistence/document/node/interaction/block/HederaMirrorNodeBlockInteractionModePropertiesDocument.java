package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_mirror_node_block_interaction_mode")
@Getter
public class HederaMirrorNodeBlockInteractionModePropertiesDocument implements BlockInteractionModePropertiesDocument {

    private Integer limitPerRequest;
    private Integer retriesPerRequest;
}
