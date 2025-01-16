package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.ExistingStockDataStrategy;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.NewStockDataStrategy;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockDataStrategy;
import com.taxah.hspd.util.constant.Exceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final TemplateAPIService templateAPIService;
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;

    @Transactional
    public StockResponseData saveStockData(String username, String ticker, LocalDate startDate, LocalDate endDate) {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        userOptional.orElseThrow(() -> new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_FORMATTED, username)));
        final User user = userOptional.get();

        List<Result> apiResults = getNewApiResults(ticker, startDate, endDate);

        Optional<StockResponseData> yourTickerOptional = stockResponseDataRepository.findByTicker(ticker);

        SaveStockDataStrategy strategy = yourTickerOptional.isPresent()
                ? new ExistingStockDataStrategy(user, yourTickerOptional.get(), resultRepository, apiResults)
                : new NewStockDataStrategy(stockResponseDataRepository, apiResults);

        return strategy.apply(user, ticker, startDate, endDate);
    }

    private List<Result> getNewApiResults(String ticker, LocalDate startDate, LocalDate endDate) {
        List<Result> apiResults = templateAPIService.getData(ticker, startDate, endDate).getResults();
        if (apiResults.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_TICKER_FOUND_FORMATTED, ticker));
        }
        return apiResults;
    }

    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        userOptional.orElseThrow(() -> new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_FORMATTED, username)));
        Optional<StockResponseData> byTicker = stockResponseDataRepository.findByTicker(ticker);
        byTicker.orElseThrow(() -> new NotFoundException(String.format(Exceptions.NO_SAVED_TICKER_FOUND_FORMATTED, ticker)));

        Long userId = userOptional.get().getId();
        List<Result> resultsByUserAndTicker = resultRepository.findResultsByUserAndTicker(userId, ticker);

        if (resultsByUserAndTicker.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_DATA_FOUND_FORMATTED, ticker));
        }
        return HistoricalStockPricesData.builder()
                .ticker(byTicker.get().getTicker())
                .results(resultsByUserAndTicker)
                .build();
    }
}
