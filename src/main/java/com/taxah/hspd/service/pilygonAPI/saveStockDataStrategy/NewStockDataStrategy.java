package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NewStockDataStrategy implements SaveStockDataStrategy {
    private final ResultRepository resultRepository;
    private final StockResponseDataRepository stockResponseDataRepository;

    @Override
    public boolean supports(Optional<StockResponseData> stockResponseData, boolean statusInPolygon) {
        if (stockResponseData.isEmpty()) {
            return statusInPolygon;
        }
        return false;
    }

    @Transactional
    @Override
    public List<Result> apply(List<Result> apiResults, List<Result> existedInDatabaseResults, StockResponseData data, LocalDate startDate, LocalDate endDate) {
        stockResponseDataRepository.save(data);
        apiResults.forEach(result -> result.setStockResponseData(data));
        List<Result> results = resultRepository.concurrentSaveAll(apiResults);
        data.setResults(apiResults);
        return results;
    }
}
