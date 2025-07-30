package io.naryo.chain.service.domain.io;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HederaLogResponse {

    private String address;
    private String bloom;
    private String contractId;
    private String data;
    private String index;
    private List<String> topics;
}
