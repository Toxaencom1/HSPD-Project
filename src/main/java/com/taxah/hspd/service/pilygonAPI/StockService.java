package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import com.taxah.hspd.service.auth.impl.UserService;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockDataStrategy;
import com.taxah.hspd.util.constant.Exceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    public static final String APPLYING_STRATEGY = "Applying strategy: {}";
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserService userService;
    private final ResultRepository resultRepository;

    @Transactional
    @CacheEvict(value = "hspd", key = "#user.username + '_' + #dataDTO.ticker")
    public StockResponseData saveStockDataByStrategy(SaveStockDataStrategy strategy,
                                                     List<Result> mutableApiResults,
                                                     User user,
                                                     StockResponseData stockResponseData,
                                                     GetStockResponseDataDTO dataDTO) {
        log.info(APPLYING_STRATEGY, strategy.getClass().getSimpleName());
        return strategy.apply(mutableApiResults, user, stockResponseData, dataDTO.getStart(), dataDTO.getEnd());
    }

    @Transactional
    @Cacheable(value = "hspd", key ="#username + '_' + #ticker" )
    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        StockResponseData stockResponseData = stockResponseDataRepository.findByTicker(ticker).orElseThrow(() ->
                new NotFoundException(String.format(Exceptions.NO_SAVED_TICKER_FOUND_F, ticker))
        );
        User user = (User) userService.loadUserByUsername(username);
        Long userId = user.getId();

        List<Result> resultsByUserAndTicker = resultRepository.findResultsByUserAndTicker(userId, ticker);

        if (resultsByUserAndTicker.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_DATA_FOUND_F, ticker));
        }
        return HistoricalStockPricesData.builder()
                .ticker(stockResponseData.getTicker())
                .results(resultsByUserAndTicker)
                .build();
    }

    public Optional<StockResponseData> getTickerInDatabase(String ticker) {
        return stockResponseDataRepository.findByTicker(ticker);
    }
}
