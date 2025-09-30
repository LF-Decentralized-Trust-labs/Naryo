package io.naryo.application.configuration.revision.diff;

import java.util.List;

public record DiffResult<T>(List<T> added, List<T> removed, List<Modified<T>> modified) {
    public record Modified<T>(T before, T after) {}
}
