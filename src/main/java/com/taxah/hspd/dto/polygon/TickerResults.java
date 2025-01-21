package com.taxah.hspd.dto.polygon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TickerResults {
    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("name")
    private String name;
}
