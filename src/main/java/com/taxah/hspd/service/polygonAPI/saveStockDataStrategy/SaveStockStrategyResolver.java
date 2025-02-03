package com.taxah.hspd.service.polygonAPI.saveStockDataStrategy;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.UnsupportedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.taxah.hspd.util.constant.Exceptions.UNSUPPORTED_TICKER_F;

@Component
@RequiredArgsConstructor
public class SaveStockStrategyResolver {
    private final List<SaveStockDataStrategy> strategies;

    public SaveStockDataStrategy resolve(String ticker, Optional<StockResponseData> optionalTicker, boolean statusInPolygon) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(optionalTicker, statusInPolygon))
                .findFirst()
                .orElseThrow(() -> new UnsupportedException(String.format(UNSUPPORTED_TICKER_F, ticker)));
    }
}
