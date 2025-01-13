package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockService {
    private final TemplateAPIService templateAPIService;
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;

    @Transactional
    public StockResponseData saveStockData(String username, String ticker, LocalDate startDate, LocalDate endDate) {
        final User user;
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isPresent()) {
            user = userOptional.get();

            List<Result> apiResults = getNewApiResults(ticker, startDate, endDate);

            Optional<StockResponseData> yourTickerOptional = stockResponseDataRepository.findByTicker(ticker);
            if (yourTickerOptional.isPresent()) {
                StockResponseData yourTicker = yourTickerOptional.get();

                addUserToExistedResults(ticker, startDate, endDate, user);

                apiResults.forEach(result -> result.setStockResponseData(yourTicker));
                Set<Result> apiResultsSet = new HashSet<>(apiResults);

                List<Result> yourTickerResults = yourTicker.getResults();
                Set<Result> existedResults = new HashSet<>(yourTickerResults);

                apiResultsSet.removeAll(existedResults);

                if (!apiResultsSet.isEmpty()) {
                    apiResultsSet.forEach(result -> result.addUser(user));
                    Set<Result> afterRemoveSet = new HashSet<>(apiResultsSet);
                    yourTickerResults.addAll(afterRemoveSet);
                } else
                    throw new AlreadyExistsException("Data already exists for " + ticker);

                return stockResponseDataRepository.save(yourTicker);
            } else {
                apiResults.forEach(result -> result.addUser(user));
                return stockResponseDataRepository.save(StockResponseData.builder()
                        .ticker(ticker)
                        .results(apiResults)
                        .build());
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    private List<Result> getNewApiResults(String ticker, LocalDate startDate, LocalDate endDate) {
        List<Result> apiResults = templateAPIService.getData(ticker, startDate, endDate).getResults();
        if (apiResults.isEmpty()) {
            throw new NotFoundException("No ticker found for " + ticker);
        }
        return apiResults;
    }

    private void addUserToExistedResults(String ticker, LocalDate startDate, LocalDate endDate, User user) {
        List<Result> existedInDataBaseResults = resultRepository.findByDateAndTicker(ticker, startDate, endDate);
        if (!existedInDataBaseResults.isEmpty()) {
            existedInDataBaseResults.forEach(result -> {
                if (!result.getUsers().contains(user)) {
                    result.getUsers().add(user);
                }
            });
            resultRepository.saveAllAndFlush(existedInDataBaseResults);
        }
    }

    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        Optional<StockResponseData> byTicker = stockResponseDataRepository.findByTicker(ticker);
        byTicker.orElseThrow(()->new NotFoundException("No saved ticker found for " + ticker));
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            List<Result> resultsByUserAndTicker = resultRepository.findResultsByUserAndTicker(id, ticker);
            return HistoricalStockPricesData.builder()
                    .ticker(byTicker.get().getTicker())
                    .results(resultsByUserAndTicker)
                    .build();
        } else
            throw new NotFoundException("User not found");
    }
}
