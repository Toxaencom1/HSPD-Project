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
import com.taxah.hspd.utils.constant.Exceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        userOptional.orElseThrow(() -> new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_FORMATTED, username)));
        user = userOptional.get();

        List<Result> apiResults = getNewApiResults(ticker, startDate, endDate);

        Optional<StockResponseData> yourTickerOptional = stockResponseDataRepository.findByTicker(ticker);
        if (yourTickerOptional.isPresent()) {
            StockResponseData yourTicker = yourTickerOptional.get();
            List<Result> existedInDatabaseResults = resultRepository.findByDateAndTicker(yourTicker.getTicker(), startDate, endDate);

            boolean usersAdded = addUserToExistedResults(user, existedInDatabaseResults);

            apiResults.forEach(result -> result.setStockResponseData(yourTicker));
            apiResults = subtractLists(apiResults, existedInDatabaseResults);

            if (!apiResults.isEmpty()) {
                apiResults.forEach(result -> result.addUser(user));
                resultRepository.saveAll(apiResults);
                return yourTicker;
            } else if (usersAdded) {
                return yourTicker;
            } else
                throw new AlreadyExistsException(String.format(Exceptions.DATA_ALREADY_EXISTS_FORMATTED, ticker, startDate, endDate));
        } else {
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

    private List<Result> getNewApiResults(String ticker, LocalDate startDate, LocalDate endDate) {
        List<Result> apiResults = templateAPIService.getData(ticker, startDate, endDate).getResults();
        if (apiResults.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_TICKER_FOUND_FORMATTED, ticker));
        }
        return apiResults;
    }

    private boolean addUserToExistedResults(User user, List<Result> existedInDataBaseResults) {
        boolean flag = false;
        if (!existedInDataBaseResults.isEmpty()) {
            for (Result result : existedInDataBaseResults) {
                if (!result.getUsers().contains(user)) {
                    result.getUsers().add(user);
                    flag = true;
                }
            }
            resultRepository.saveAllAndFlush(existedInDataBaseResults);
        }
        return flag;
    }

    public List<Result> subtractLists(List<Result> mainList, List<Result> subtractiveList) {
        Set<Result> subtractedSet = new HashSet<>(subtractiveList);
        return mainList.stream()
                .filter(result -> !subtractedSet.contains(result))
                .collect(Collectors.toList());
    }

    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        Optional<StockResponseData> byTicker = stockResponseDataRepository.findByTicker(ticker);
        byTicker.orElseThrow(() -> new NotFoundException(String.format(Exceptions.NO_SAVED_TICKER_FOUND_FORMATTED, ticker)));
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            List<Result> resultsByUserAndTicker = resultRepository.findResultsByUserAndTicker(id, ticker);
            return HistoricalStockPricesData.builder()
                    .ticker(byTicker.get().getTicker())
                    .results(resultsByUserAndTicker)
                    .build();
        } else
            throw new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_FORMATTED, username));
    }
}
