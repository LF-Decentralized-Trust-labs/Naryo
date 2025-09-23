package io.naryo.domain.common.normalization;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class NormalizationUtil {

    // ZW chars: ZWSP, ZWNJ, ZWJ, WJ, BOM
    private static final Pattern INVISIBLES =
            Pattern.compile("[\\u200B\\u200C\\u200D\\u2060\\uFEFF]");

    private NormalizationUtil() {}

    public static String normalize(String in) {
        if (in == null) return null;
        // lower case
        in = in.toLowerCase(Locale.ROOT);
        // line endings
        in = in.replace("\r\n", "\n").replace("\r", "\n");
        // remove BOM if present
        if (!in.isEmpty() && in.charAt(0) == '\uFEFF') in = in.substring(1);
        // remove zero-width
        in = INVISIBLES.matcher(in).replaceAll("");
        // NBSP -> space
        in = in.replace('\u00A0', ' ');
        // trim
        in = in.trim();
        return Normalizer.normalize(in, Normalizer.Form.NFC);
    }

    public static <E> LinkedHashSet<E> normalize(Set<E> in, Comparator<E> cmp) {
        return in.stream()
                .filter(Objects::nonNull)
                .sorted(cmp)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static <E> List<E> normalize(List<E> in, Comparator<E> cmp) {
        if (in == null) return List.of();
        return in.stream().filter(Objects::nonNull).sorted(cmp).toList();
    }
}
