package com.taxah.hspd.service.polygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    @Transactional
    @Override
    public List<Result> apply(List<Result> apiResults, List<Result> existedInDatabaseResults, StockResponseData data) {
        apiResults.forEach(result -> result.setStockResponseData(data));
        apiResults = subtractLists(apiResults, existedInDatabaseResults);

        if (!apiResults.isEmpty()) {
            List<Result> results = resultRepository.concurrentSaveAll(apiResults);
            existedInDatabaseResults.addAll(results);
        }

        return existedInDatabaseResults;
    }

    private List<Result> subtractLists(List<Result> mainList, List<Result> subtractiveList) {
        Set<Result> subtractedSet = new HashSet<>(subtractiveList);
        return mainList.stream()
                .filter(result -> !subtractedSet.contains(result))
                .collect(Collectors.toList());
    }
}
