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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents contract event specification, to be used when registering a new filter.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Embeddable
@Data
@EqualsAndHashCode
public class ContractEventSpecification implements Serializable {

    private String eventName;

    @ElementCollection(fetch = FetchType.EAGER)
    // See answer in
    // https://stackoverflow.com/questions/51835604/jpa-elementcollection-within-embeddable-not-persisted
    private List<ParameterDefinition> indexedParameterDefinitions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ParameterDefinition> nonIndexedParameterDefinitions = new ArrayList<>();

    private String eventSignature;

    private String web3EventSmartContractClass;
}
