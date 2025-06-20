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

package io.naryo.model;

import java.math.BigInteger;

import io.naryo.dto.event.filter.ContractEventFilter;
import io.reactivex.disposables.Disposable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterSubscription {

    private ContractEventFilter filter;

    private Disposable subscription;

    private BigInteger startBlock;

    public FilterSubscription(ContractEventFilter filter, Disposable subscription) {
        this.filter = filter;
        this.subscription = subscription;
    }
}
