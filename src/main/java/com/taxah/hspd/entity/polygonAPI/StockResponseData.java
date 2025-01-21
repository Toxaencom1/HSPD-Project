package com.taxah.hspd.entity.polygonAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_response_data")
public class StockResponseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("ticker")
    private String ticker;

    @OneToMany(mappedBy = "stockResponseData", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonProperty("results")
    @BatchSize(size = 450)
    private List<Result> results = new ArrayList<>();

    public void addResult(Result result) {
        results.add(result);
        result.setStockResponseData(this);
    }
}
