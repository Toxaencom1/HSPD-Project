package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.auth.impl.UserService;
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
    private final UserService userService;
    private final SaveStockStrategyResolver strategyResolver;

    public void saveStock(String username, GetStockResponseDataDTO dataDTO) {
        String ticker = dataDTO.getTicker();
        User existedUser = userService.findByUsernameWithResults(username);
        Optional<StockResponseData> alreadyExist = stockService.getTickerInDatabase(ticker);
        boolean statusInPolygon = alreadyExist
                .map(value -> true)
                .orElseGet(() -> polygonService.checkTickerInPolygon(ticker));
        List<Result> results = polygonService.getNewApiResults(dataDTO);

        SaveStockDataStrategy chosenStrategy = strategyResolver.resolve(ticker, alreadyExist, statusInPolygon);

        stockService.saveStockDataByStrategy(chosenStrategy,
                results,
                existedUser,
                alreadyExist.orElseGet(() -> StockResponseData.builder().ticker(ticker).build()),
                dataDTO
        );
    }

    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        return stockService.getSavedInfo(username, ticker);
    }
}
