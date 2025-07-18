package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_mirror_node_block_interaction")
@Getter
@Setter
public class HederaMirrorNodeBlockInteractionPropertiesDocument
        extends BlockInteractionPropertiesDocument
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private Integer limitPerRequest;
    private Integer retriesPerRequest;
}
