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

package io.naryo.dto.message;

import io.naryo.dto.block.BlockDetails;

public class BlockEvent extends AbstractMessage<BlockDetails> {
    public static final String TYPE = "BLOCK";

    public BlockEvent() {}

    public BlockEvent(BlockDetails details) {
        super(details.getHash(), TYPE, details);
    }
}
