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

package io.naryo.integration;

import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pulsar")
@Data
public class PulsarSettings {
    @Data
    public static class Authentication {
        private String pluginClassName;
        private Map<String, String> params;
    }

    @Data
    public static class Topics {
        private String blockEvents;

        private String contractEvents;

        private String transactionEvents;

        private String messageEvents;
    }

    private Map<String, Object> config;
    private Authentication authentication;

    private Topics topic;
}
