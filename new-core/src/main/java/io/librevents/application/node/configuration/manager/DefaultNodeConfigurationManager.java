package io.librevents.application.node.configuration.manager;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import io.librevents.application.node.configuration.provider.NodeConfigurationProvider;
import io.librevents.domain.node.Node;

import static java.util.stream.Collectors.toMap;

public final class DefaultNodeConfigurationManager implements NodeConfigurationManager {

    private final List<NodeConfigurationProvider> providers;

    public DefaultNodeConfigurationManager(List<NodeConfigurationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Collection<Node> load() {
        return providers.stream()
                .sorted(Comparator.comparingInt(NodeConfigurationProvider::priority))
                .flatMap(provider -> provider.load().stream())
                .collect(
                        toMap(
                                Node::getName,
                                Function.identity(),
                                (oldNode, newNode) -> newNode,
                                LinkedHashMap::new))
                .values();
    }
}
