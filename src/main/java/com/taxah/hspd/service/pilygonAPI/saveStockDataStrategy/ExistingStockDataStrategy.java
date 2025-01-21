package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ExistingStockDataStrategy implements SaveStockDataStrategy {
    private final ResultRepository resultRepository;

    @Override
    public boolean supports(Optional<StockResponseData> stockResponseData, boolean statusInPolygon) {
        return stockResponseData.isPresent();
    }

    @Override
    public StockResponseData apply(List<Result> apiResults, User user, StockResponseData data, LocalDate startDate, LocalDate endDate) {
        List<Result> existedInDatabaseResults = resultRepository.findByDateAndTicker(data.getTicker(), startDate, endDate);

        boolean userAdded = addUserToExistedResults(user, existedInDatabaseResults);

        apiResults.forEach(result -> result.setStockResponseData(data));
        apiResults = subtractLists(apiResults, existedInDatabaseResults);

        if (!apiResults.isEmpty()) {
            apiResults.forEach(result -> result.addUser(user));  // Добавляем пользователя к каждому результату
            resultRepository.saveAll(apiResults);
        }

        if (!apiResults.isEmpty() || userAdded) {
            return data;
        }

        throw new AlreadyExistsException(String.format(Exceptions.DATA_ALREADY_EXISTS_F, data.getTicker(), startDate, endDate));
    }

    private boolean addUserToExistedResults(User user, List<Result> results) {
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
