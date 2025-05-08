package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;

public interface Page<T> {
    List<T> getResults();

    Map<String, String> getLinks();
}
