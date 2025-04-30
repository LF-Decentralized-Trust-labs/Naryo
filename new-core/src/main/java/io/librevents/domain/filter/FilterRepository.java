package io.librevents.domain.filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FilterRepository {

    Optional<Filter> findById(UUID id);

    List<Filter> findAllById(List<UUID> ids);

    List<Filter> findByNodeId(UUID nodeId);
}
