package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockDataStrategy;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockStrategyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolygonFacade {
    private final StockService stockService;
    private final PolygonService polygonService;
    private final SaveStockStrategyResolver strategyResolver;

    public void saveStock(String username, GetStockResponseDataDTO dataDTO) {
        String tickerName = dataDTO.getTicker();
        Optional<StockResponseData> alreadyExistTicker = stockService.getTickerInDatabase(tickerName);

        boolean statusInPolygon = alreadyExistTicker
                .map(value -> true)
                .orElseGet(() -> polygonService.checkTickerInPolygon(tickerName));
        List<Result> apiResults = polygonService.getNewApiResults(dataDTO);

        SaveStockDataStrategy chosenStrategy = strategyResolver.resolve(tickerName, alreadyExistTicker, statusInPolygon);

        stockService.saveStockData(chosenStrategy, apiResults, username, tickerName, alreadyExistTicker, dataDTO);
    }

    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        return stockService.getSavedInfo(username, ticker);
    }
}
