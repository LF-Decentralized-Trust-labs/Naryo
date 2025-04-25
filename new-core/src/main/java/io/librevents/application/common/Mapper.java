package io.librevents.application.common;

import java.util.Map;

public interface Mapper<S, O> {

    O map(S source);

    O map(S source, Map<String, Object> additionalProperties);
}
