package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockGetService stockGetService;
    private final StockResponseDataRepository stockResponseDataRepository;
    private final ResultRepository resultRepository;

    public StockResponseData getStock(String ticker, LocalDate dateFrom, LocalDate dateTo) {
        return stockGetService.getData(ticker, dateFrom, dateTo);
    }

    @Transactional
    public StockResponseData saveStockData(String ticker, LocalDate startDate, LocalDate endDate) {
        // Ищем или создаем StockResponseData
        Optional<StockResponseData> stockResponseData = stockResponseDataRepository.findByTicker(ticker);
        List<Result> results = getStock(ticker, startDate, endDate).getResults();

        if (stockResponseData.isPresent()) {
            results.forEach(result -> result.setStockResponseData(stockResponseData.get()));
            Set<Result> newResultsSet = new HashSet<>(results);
            Set<Result> existedResults = new HashSet<>(stockResponseData.get().getResults()); //resultRepository.findByDateAndTicker(ticker, startDate, endDate);

            // Отфильтровываем новые результаты, избегая дубликатов
            boolean b = newResultsSet.removeAll(existedResults);
            System.out.println("\n" + b + "\n");
            System.out.println("NewResultsSet with deleted duplicates = " + newResultsSet + " , size" + newResultsSet.size());
            stockResponseData.get().getResults().addAll(newResultsSet);
            return stockResponseDataRepository.save(stockResponseData.get());
        } else {
            return stockResponseDataRepository.save(StockResponseData.builder()
                    .ticker(ticker)
                    .results(results)
                    .build());
        }
    }
}
