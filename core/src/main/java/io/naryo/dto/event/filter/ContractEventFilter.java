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

package io.naryo.dto.event.filter;

import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.naryo.constant.Constants;
import io.naryo.dto.converter.HashMapConverter;
import io.naryo.dto.event.filter.correlation_id.ParameterCorrelationIdStrategy;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the details of a contract event filter.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Document
@Entity
@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractEventFilter {

    @Id private String id;

    private String contractAddress;

    private String node = Constants.DEFAULT_NODE_NAME;

    @Embedded private ContractEventSpecification eventSpecification;

    @Embedded private ParameterCorrelationIdStrategy correlationIdStrategy;

    private BigInteger startBlock;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> extension;
}
