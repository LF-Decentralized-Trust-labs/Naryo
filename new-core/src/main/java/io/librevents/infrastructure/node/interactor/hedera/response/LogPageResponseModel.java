package io.librevents.infrastructure.node.interactor.hedera.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LogPageResponseModel implements Page<LogResponseModel> {

    private final List<LogResponseModel> logs;
    private final Map<String, String> links;

    public LogPageResponseModel(List<LogResponseModel> logs, Map<String, String> links) {
        Objects.requireNonNull(logs, "logs must not be null");
        Objects.requireNonNull(links, "links must not be null");
        this.logs = logs;
        this.links = links;
    }

    @Override
    public List<LogResponseModel> getResults() {
        return logs;
    }

    @Override
    public Map<String, String> getLinks() {
        return links;
    }
}
