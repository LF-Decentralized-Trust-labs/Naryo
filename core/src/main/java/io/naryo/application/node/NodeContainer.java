package io.naryo.application.node;

import java.io.IOException;
import java.util.Set;

public record NodeContainer(Set<NodeRunner> nodes) {

    public void start() throws IOException {
        for (var node : nodes) {
            node.run();
        }
    }
}
