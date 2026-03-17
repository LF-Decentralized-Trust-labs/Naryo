package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.MaxCustomFee;
import jakarta.persistence.Converter;

@Converter
public class MaxCustomFeeListConverter extends BaseJsonConverter<List<MaxCustomFee>> {

    @Override
    protected TypeReference<List<MaxCustomFee>> getType() {
        return new TypeReference<>() {};
    }
}
