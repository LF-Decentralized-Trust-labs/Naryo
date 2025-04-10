package io.librevents.domain.node.interaction.block.hedera;

public record LimitPerRequest(int value) {
    public LimitPerRequest {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }
}
