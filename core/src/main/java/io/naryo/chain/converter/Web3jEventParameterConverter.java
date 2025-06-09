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

package io.naryo.chain.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.naryo.dto.event.parameter.ArrayParameter;
import io.naryo.dto.event.parameter.EventParameter;
import io.naryo.dto.event.parameter.NumberParameter;
import io.naryo.dto.event.parameter.StringParameter;
import io.naryo.settings.NaryoSettings;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

/**
 * Converts Web3j Type objects into Naryo EventParameter objects.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Component("web3jEventParameterConverter")
public class Web3jEventParameterConverter implements EventParameterConverter<Type> {

    private final Map<String, EventParameterConverter<Type>> typeConverters = new HashMap<>();
    private final NaryoSettings settings;

    public Web3jEventParameterConverter(NaryoSettings settings) {
        typeConverters.put(
                "address",
                type ->
                        new StringParameter(
                                type.getTypeAsString(), Keys.toChecksumAddress(type.toString())));

        registerNumberConverters("uint");
        registerNumberConverters("int");
        registerBytesConverters();

        typeConverters.put("byte", this::convertBytesType);
        typeConverters.put(
                "bool",
                type ->
                        new NumberParameter(
                                type.getTypeAsString(),
                                Boolean.TRUE.equals(type.getValue())
                                        ? BigInteger.ONE
                                        : BigInteger.ZERO));
        typeConverters.put(
                "string",
                type ->
                        new StringParameter(
                                type.getTypeAsString(), trim((String) type.getValue())));
        typeConverters.put(
                "bytes",
                type ->
                        new StringParameter(
                                type.getTypeAsString(),
                                Numeric.toHexString((byte[]) type.getValue())));

        this.settings = settings;
    }

    @Override
    public EventParameter convert(Type toConvert) {
        final EventParameterConverter<Type> typeConverter =
                typeConverters.get(toConvert.getTypeAsString().toLowerCase());

        if (typeConverter == null) {
            // Type might be an array, in which case the type will be the array type class
            if (toConvert instanceof Array<?> theArray) {
                return convertArray(theArray);
            }

            throw new TypeConversionException("Unsupported type: " + toConvert.getTypeAsString());
        }

        return typeConverter.convert(toConvert);
    }

    private void registerNumberConverters(String prefix) {
        for (int i = 8; i <= 256; i = i + 8) {
            typeConverters.put(
                    prefix + i,
                    type ->
                            new NumberParameter(
                                    type.getTypeAsString(), (BigInteger) type.getValue()));
        }
    }

    private void registerBytesConverters() {
        for (int i = 1; i <= 32; i = i + 1) {
            typeConverters.put("bytes" + i, this::convertBytesType);
        }
    }

    private EventParameter<?> convertArray(Array<?> toConvert) {
        final ArrayList<EventParameter<?>> convertedArray = new ArrayList<>();

        toConvert.getValue().forEach(arrayEntry -> convertedArray.add(convert(arrayEntry)));

        return new ArrayParameter<>(
                toConvert.getTypeAsString().replace("[]", "").toLowerCase(), convertedArray);
    }

    private EventParameter convertBytesType(Type bytesType) {
        if (settings.isBytesToAscii()) {
            return new StringParameter(
                    bytesType.getTypeAsString(), trim(new String((byte[]) bytesType.getValue())));
        }

        return new StringParameter(
                bytesType.getTypeAsString(),
                trim(Numeric.toHexString((byte[]) bytesType.getValue())));
    }

    private String trim(String toTrim) {
        return toTrim.trim().replace("\\u0000", "");
    }
}
