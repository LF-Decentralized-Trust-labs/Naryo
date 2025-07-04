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

package io.naryo.settings;

import java.math.BigInteger;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class NaryoSettings {

    private boolean bytesToAscii;

    private BigInteger syncBatchSize;

    public NaryoSettings(
            @Value("${broadcaster.bytesToAscii:false}") boolean bytesToAscii,
            @Value("${ethereum.sync.batchSize:100000}") String syncBatchSize) {
        this.bytesToAscii = bytesToAscii;
        this.syncBatchSize = new BigInteger(syncBatchSize);
    }
}
