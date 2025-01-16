package com.taxah.hspd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxah.hspd.entity.polygonAPI.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "DTO для возврата данных об акциях.")
public class HistoricalStockPricesData {
    @Schema(description = "Уникальный идентификатор акции.", example = "AAPL")
    private String ticker;
    @Schema(description = "Список результатов.", example = "\"results\": [\n" +
            "        {\n" +
            "            \"t\": \"2023-01-17\",\n" +
            "            \"o\": 134.83,\n" +
            "            \"c\": 135.94,\n" +
            "            \"h\": 137.29,\n" +
            "            \"l\": 134.13\n" +
            "        }]")
    private List<Result> results;

    @Schema(description = "Количество результатов в списке.", example = "12")
    private Integer resultsCount;

    @JsonProperty("resultsCount")
    public Integer getResultsCount() {
        return results != null ? results.size() : 0;
    }
}
