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

package io.naryo.chain.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.event.filter.correlation_id.CorrelationIdType;
import io.naryo.dto.event.filter.correlation_id.ParameterCorrelationIdStrategy;
import io.naryo.utils.ModelMapperFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Keys;

/**
 * Event Filter Configuration
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Configuration
@ConfigurationProperties
@Data
public class EventFilterConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(EventFilterConfiguration.class);

    private List<EventFilterConfig> eventFilters;

    // Can't seem to be able to unmarshall from config to an interface using Spring.
    // This method converts CorrelationId to CorrelationId strategy
    public List<ContractEventFilter> getConfiguredEventFilters() {
        List<ContractEventFilter> filtersToReturn = new ArrayList<>();

        if (eventFilters != null) {
            final ModelMapper mapper = ModelMapperFactory.getModelMapper();

            eventFilters.forEach(
                    configFilter -> {
                        final ContractEventFilter contractEventFilter = new ContractEventFilter();
                        mapper.map(configFilter, contractEventFilter);
                        contractEventFilter.setContractAddress(
                                Keys.toChecksumAddress(contractEventFilter.getContractAddress()));

                        if (configFilter.getCorrelationId() != null) {
                            contractEventFilter.setCorrelationIdStrategy(
                                    configFilter.getCorrelationId().toStrategy());
                        }
                        contractEventFilter.setContractAddress(
                                Keys.toChecksumAddress(contractEventFilter.getContractAddress()));
                        filtersToReturn.add(contractEventFilter);
                    });
        }

        return filtersToReturn;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class EventFilterConfig extends ContractEventFilter {
        private CorrelationId correlationId;
    }

    @Data
    public static class CorrelationId {
        private String type;

        private int index;

        private Map<String, Callable<ParameterCorrelationIdStrategy>> strategyCreatorMap =
                new HashMap<>();

        public CorrelationId() {
            strategyCreatorMap.put(
                    CorrelationIdType.INDEXED_PARAMETER.name(),
                    () ->
                            new ParameterCorrelationIdStrategy(
                                    CorrelationIdType.INDEXED_PARAMETER, index));

            strategyCreatorMap.put(
                    CorrelationIdType.NON_INDEXED_PARAMETER.name(),
                    () ->
                            new ParameterCorrelationIdStrategy(
                                    CorrelationIdType.NON_INDEXED_PARAMETER, index));
        }

        public ParameterCorrelationIdStrategy toStrategy() {
            try {
                return strategyCreatorMap.get(type).call();
            } catch (Exception e) {
                LOG.error(
                        "Error when obtaining correlation id strategy...application.yml probably incorrect",
                        e);
                return null;
            }
        }
    }
}
