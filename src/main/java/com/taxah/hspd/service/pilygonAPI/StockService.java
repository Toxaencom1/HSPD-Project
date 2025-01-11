package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockGetService stockGetService;

    public StockResponseData getStock(String ticker, LocalDate dateFrom, LocalDate dateTo) {
        return stockGetService.getData(ticker, dateFrom, dateTo);
    }
}
