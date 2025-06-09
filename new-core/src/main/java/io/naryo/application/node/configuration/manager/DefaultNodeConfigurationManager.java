package io.naryo.application.node.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionConfigurationProvider;
import io.naryo.domain.node.Node;

import static java.util.stream.Collectors.toMap;

public final class DefaultNodeConfigurationManager
        extends BaseCollectionConfigurationManager<Node, String>
        implements NodeConfigurationManager {

    public DefaultNodeConfigurationManager(
            List<? extends CollectionConfigurationProvider<Node>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<Node, ?, Map<String, Node>> getCollector() {
        return toMap(
                node -> node.getName().value(),
                Function.identity(),
                Node::merge,
                LinkedHashMap::new);
    }
}
