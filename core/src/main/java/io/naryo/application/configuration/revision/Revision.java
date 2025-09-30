package io.naryo.application.configuration.revision;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public record Revision<T>(long version, String hash, Collection<T> domainItems) {

    public Revision {
        Objects.requireNonNull(domainItems, "Items cannot be null");
        Objects.requireNonNull(hash, "Revision hash cannot be null");
        domainItems = List.copyOf(domainItems);
        if (version < 0) {
            throw new IllegalArgumentException("Number cannot be negative");
        }
        if (hash.isBlank()) {
            throw new IllegalArgumentException("Revision hash cannot be blank");
        }
    }
}
