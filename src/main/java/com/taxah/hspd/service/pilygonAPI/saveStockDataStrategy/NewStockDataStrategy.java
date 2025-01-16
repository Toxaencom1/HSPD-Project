package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class NewStockDataStrategy implements SaveStockDataStrategy {
    private final StockResponseDataRepository stockResponseDataRepository;
    private final List<Result> apiResults;

    @Override
    public StockResponseData apply(User user, String ticker, LocalDate startDate, LocalDate endDate) {
        StockResponseData newTicker = StockResponseData.builder()
                .ticker(ticker)
                .build();
        apiResults.forEach(result -> {
            result.addUser(user);
            result.setStockResponseData(newTicker);
        });
        newTicker.setResults(apiResults);
        return stockResponseDataRepository.save(newTicker);
    }
}
