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
import java.util.List;

import io.naryo.model.TransactionMonitoringSpec;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
public class TransactionFilterConfiguration {
    private List<TransactionMonitoringSpec> transactionFilters;

    public List<TransactionMonitoringSpec> getConfiguredTransactionFilters() {
        List<TransactionMonitoringSpec> filtersToReturn = new ArrayList<>();

        if (transactionFilters == null) {
            return filtersToReturn;
        }

        transactionFilters.forEach(
                configFilter -> {
                    final TransactionMonitoringSpec contractTransactionFilter =
                            new TransactionMonitoringSpec(
                                    configFilter.getType(),
                                    configFilter.getTransactionIdentifierValue(),
                                    configFilter.getNodeName(),
                                    configFilter.getStatuses(),
                                    configFilter.getExtension());
                    filtersToReturn.add(contractTransactionFilter);
                });

        return filtersToReturn;
    }
}
