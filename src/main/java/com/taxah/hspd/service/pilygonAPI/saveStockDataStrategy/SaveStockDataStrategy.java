package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;

import java.time.LocalDate;

public interface SaveStockDataStrategy {
    StockResponseData apply(User user, String ticker, LocalDate startDate, LocalDate endDate);
}
