package com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SaveStockDataStrategyFactory {
    private final ExistingStockDataStrategy existingStockDataStrategy;
    private final NewStockDataStrategy newStockDataStrategy;

    public SaveStockDataStrategy getStrategy(Optional<StockResponseData> dataOptional) {
        return dataOptional.isPresent() ? existingStockDataStrategy : newStockDataStrategy;
    }
}
