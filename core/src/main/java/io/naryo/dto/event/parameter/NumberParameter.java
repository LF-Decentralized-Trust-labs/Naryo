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

package io.naryo.dto.event.parameter;

import java.math.BigInteger;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A number based EventParameter, represented by a BigInteger.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Data
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NumberParameter extends AbstractEventParameter<BigInteger> {

    public NumberParameter(String type, BigInteger value) {
        super(type, value);
    }

    @Override
    public String getValueString() {
        return getValue().toString();
    }
}
