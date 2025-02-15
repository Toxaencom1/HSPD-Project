package com.taxah.hspd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxah.hspd.entity.polygonAPI.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Schema(description = "DTO для возврата данных об акциях.")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HistoricalStockPricesData implements Serializable {
    @Schema(description = "Уникальный идентификатор акции.", example = "AAPL")
    private String ticker;
    @Schema(description = "Список результатов.", example = """
            "results": [
                    {
                        "t": "2023-01-17",
                        "o": 134.830,
                        "c": 135.940,
                        "h": 137.290,
                        "l": 134.130
                    }]""")
    private List<Result> results;

    @Schema(description = "Количество результатов в списке.", example = "12")
    private Integer resultsCount;

    @JsonProperty("resultsCount")
    public Integer getResultsCount() {
        return results != null ? results.size() : 0;
    }
}
