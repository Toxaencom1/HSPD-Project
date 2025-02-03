package com.taxah.hspd.service.polygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;

import java.util.List;
import java.util.Optional;

public interface SaveStockDataStrategy {
    boolean supports(Optional<StockResponseData> stockResponseData, boolean statusInPolygon);

    List<Result> apply(List<Result> apiResults, List<Result> existedInDatabaseResults, StockResponseData data);
}
