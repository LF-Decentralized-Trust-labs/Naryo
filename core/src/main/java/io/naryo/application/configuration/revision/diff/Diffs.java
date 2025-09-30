package io.naryo.application.configuration.revision.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Diffs {

    private Diffs() {}

    public static <T, K> DiffResult<T> diff(
            Collection<T> before,
            Collection<T> after,
            Function<T, K> idFn,
            BiPredicate<T, T> semanticallyEqual) {

        var oldMap = before.stream().collect(Collectors.toMap(idFn, Function.identity()));
        var newMap = after.stream().collect(Collectors.toMap(idFn, Function.identity()));

        var added = new ArrayList<T>();
        var removed = new ArrayList<T>();
        var modified = new ArrayList<DiffResult.Modified<T>>();

        for (var e : newMap.entrySet()) {
            var k = e.getKey();
            var newItem = e.getValue();
            var oldItem = oldMap.get(k);
            if (oldItem == null) {
                added.add(newItem);
            } else if (!semanticallyEqual.test(oldItem, newItem)) {
                modified.add(new DiffResult.Modified<>(oldItem, newItem));
            }
        }

        for (var k : oldMap.keySet()) {
            if (!newMap.containsKey(k)) removed.add(oldMap.get(k));
        }
        return new DiffResult<>(List.copyOf(added), List.copyOf(removed), List.copyOf(modified));
    }
}
