package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NewStockDataStrategy implements SaveStockDataStrategy {
    private final StockResponseDataRepository stockResponseDataRepository;

    @Override
    public boolean supports(Optional<StockResponseData> optionalTicker, boolean statusInPolygon) {
        if (optionalTicker.isEmpty()) {
            return statusInPolygon;
        }
        return false;
    }

    @Override
    public StockResponseData apply(List<Result> apiResults, User user, StockResponseData data, LocalDate startDate, LocalDate endDate) {
        apiResults.forEach(result -> {
            result.addUser(user);
            result.setStockResponseData(data);
        });
        data.setResults(apiResults);
        return stockResponseDataRepository.save(data);
    }
}
