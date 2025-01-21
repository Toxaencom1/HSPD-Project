package com.taxah.hspd.dto.polygon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TickerResponseData {
    @JsonProperty("results")
    private TickerResults results;
    @JsonProperty("status")
    private String status;
}
