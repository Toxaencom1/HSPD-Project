package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
public class ExistingStockDataStrategy implements SaveStockDataStrategy {
    private final User user;
    private final StockResponseData existingData;
    private final ResultRepository resultRepository;
    private List<Result> apiResults;

    @Override
    public StockResponseData apply(User user, String ticker, LocalDate startDate, LocalDate endDate) {
        List<Result> existedInDatabaseResults = resultRepository.findByDateAndTicker(ticker, startDate, endDate);

        boolean usersAdded = addUserToExistedResults(existedInDatabaseResults);

        apiResults.forEach(result -> result.setStockResponseData(existingData));
        apiResults = subtractLists(apiResults, existedInDatabaseResults);

        if (!apiResults.isEmpty()) {
            apiResults.forEach(result -> result.addUser(user));
            resultRepository.saveAll(apiResults);
            return existingData;
        } else if (usersAdded) {
            return existingData;
        } else
            throw new AlreadyExistsException(String.format(Exceptions.DATA_ALREADY_EXISTS_FORMATTED, ticker, startDate, endDate));
    }

    private boolean addUserToExistedResults(List<Result> results) {
        // Stream api не подходит из-за необходимости работать с эффективно финальной переменной
        boolean flag = false;
        for (Result result : results) {
            if (!result.getUsers().contains(user)) {
                result.getUsers().add(user);
                flag = true;
            }
        }
        resultRepository.saveAllAndFlush(results);
        return flag;
    }

    private List<Result> subtractLists(List<Result> mainList, List<Result> subtractiveList) {
        Set<Result> subtractedSet = new HashSet<>(subtractiveList);
        return mainList.stream()
                .filter(result -> !subtractedSet.contains(result))
                .collect(Collectors.toList());
    }
}
