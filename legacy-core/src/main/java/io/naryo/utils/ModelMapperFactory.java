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

package io.naryo.utils;

import io.naryo.chain.service.domain.converter.BlockResponseToHederaBlockConverter;
import io.naryo.chain.service.domain.converter.ContractResultResponseToTransactionConverter;
import io.naryo.chain.service.domain.converter.EthBlockToWeb3jBlockConverter;
import org.modelmapper.ModelMapper;

/**
 * A singleton factory for creating ModelMapper instances.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
public final class ModelMapperFactory {

    private static final ModelMapper MODEL_MAPPER_INSTANCE = createModelMapper();

    private ModelMapperFactory() {}

    /**
     * Provides a shared, pre-configured ModelMapper instance.
     *
     * @return a singleton ModelMapper instance.
     */
    public static ModelMapper getModelMapper() {
        return MODEL_MAPPER_INSTANCE;
    }

    /**
     * Creates and configures a ModelMapper instance.
     *
     * @return a fully configured ModelMapper.
     */
    private static ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new EthBlockToWeb3jBlockConverter());
        modelMapper.addConverter(new BlockResponseToHederaBlockConverter());
        modelMapper.addConverter(new ContractResultResponseToTransactionConverter());
        return modelMapper;
    }
}
