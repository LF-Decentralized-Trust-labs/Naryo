/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.config;

import java.util.EnumSet;
import java.util.regex.Pattern;

import io.micrometer.common.lang.Nullable;
import io.micrometer.core.instrument.Meter.Type;
import io.micrometer.core.instrument.config.NamingConvention;
import io.prometheus.metrics.model.snapshots.PrometheusNaming;
import org.jetbrains.annotations.NotNull;

public class CustomNamingConvention implements NamingConvention {

    private static final Pattern nameChars = Pattern.compile("[^a-zA-Z0-9_:]");
    private static final Pattern tagKeyChars = Pattern.compile("\\W");
    private static final String NARYO_PREFIX = "naryo_";
    private static final String SECONDS_SUFFIX = "_seconds";
    private final String timerSuffix;

    public CustomNamingConvention() {
        this("");
    }

    public CustomNamingConvention(String timerSuffix) {
        this.timerSuffix = timerSuffix;
    }

    @NotNull
    public String name(@NotNull String name, @NotNull Type type, @Nullable String baseUnit) {
        String conventionName =
                NARYO_PREFIX + NamingConvention.snakeCase.name(name, type, baseUnit);
        boolean needsTotalSuffix =
                EnumSet.of(Type.DISTRIBUTION_SUMMARY, Type.COUNTER).contains(type);
        boolean isTimer = EnumSet.of(Type.TIMER, Type.LONG_TASK_TIMER).contains(type);

        if (needsTotalSuffix && !conventionName.endsWith("_total")) {
            conventionName += "_total";
        }

        if (type == Type.GAUGE && baseUnit != null && !conventionName.endsWith("_" + baseUnit)) {
            conventionName += "_" + baseUnit;
        }

        if (isTimer) {
            if (conventionName.endsWith(timerSuffix)) {
                conventionName += SECONDS_SUFFIX;
            } else if (!conventionName.endsWith(SECONDS_SUFFIX)) {
                conventionName += timerSuffix + SECONDS_SUFFIX;
            }
        }

        // Ensure valid Prometheus metric naming
        String sanitized = nameChars.matcher(conventionName).replaceAll("_");
        sanitized = PrometheusNaming.sanitizeMetricName(sanitized);

        // Ensure the name starts with a letter
        if (!Character.isLetter(sanitized.charAt(0))) {
            sanitized = "m_" + sanitized;
        }

        return sanitized;
    }

    @NotNull
    @Override
    public String tagKey(@NotNull String key) {
        String conventionKey = NamingConvention.snakeCase.tagKey(key);
        String sanitized = tagKeyChars.matcher(conventionKey).replaceAll("_");
        if (!Character.isLetter(sanitized.charAt(0))) {
            sanitized = "m_" + sanitized;
        }

        return sanitized;
    }
}
