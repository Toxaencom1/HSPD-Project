package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaveStockDataStrategy {
    boolean supports(Optional<StockResponseData> stockResponseData, boolean statusInPolygon);

    StockResponseData apply(List<Result> apiResults, User user, StockResponseData data, LocalDate startDate, LocalDate endDate);
}
