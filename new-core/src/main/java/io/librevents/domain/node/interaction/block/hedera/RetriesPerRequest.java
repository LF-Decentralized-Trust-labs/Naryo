package io.librevents.domain.node.interaction.block.hedera;

public record RetriesPerRequest(int value) {
    public RetriesPerRequest {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }
}
