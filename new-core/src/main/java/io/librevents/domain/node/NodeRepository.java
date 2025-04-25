package io.librevents.domain.node;

import java.util.Optional;
import java.util.UUID;

public interface NodeRepository {

    Optional<Node> findById(UUID id);

}
