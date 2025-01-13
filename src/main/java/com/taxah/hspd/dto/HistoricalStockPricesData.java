package com.taxah.hspd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxah.hspd.entity.polygonAPI.Result;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HistoricalStockPricesData {
    private String ticker;
    private List<Result> results;
    private Integer resultsCount;

    @JsonProperty("resultsCount")
    public Integer getResultsCount() {
        return results != null ? results.size() : 0;
    }
}
