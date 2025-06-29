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

package io.naryo.dto.event.filter.correlation_id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.dto.event.ContractEventDetails;
import jakarta.persistence.Embeddable;

/**
 * A strategy for obtaining a correlation id for a given contract event.
 *
 * <p>This is particularly useful when used with a Kafka broadcaster as you can configure the system
 * so that events with particular parameter values are always sent to the same partition.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Embeddable
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CorrelationIdStrategy {
    CorrelationIdType getType();

    @JsonIgnore
    String getCorrelationId(ContractEventDetails contractEvent);
}
