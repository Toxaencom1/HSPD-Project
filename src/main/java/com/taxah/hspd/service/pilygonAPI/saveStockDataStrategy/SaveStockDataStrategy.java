package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaveStockDataStrategy {
    boolean supports(Optional<StockResponseData> stockResponseData, boolean statusInPolygon);

    @Transactional
    List<Result> apply(List<Result> apiResults, List<Result> existedInDatabaseResults, StockResponseData data, LocalDate startDate, LocalDate endDate);
}
