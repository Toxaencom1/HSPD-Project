package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockDataStrategyFactory;
import com.taxah.hspd.util.constant.Exceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final SaveStockDataStrategyFactory strategyFactory;

    @Transactional
    public StockResponseData saveStockData(List<Result> mutableApiResults, String username, GetStockResponseDataDTO dataDTO) {
        if (mutableApiResults == null || mutableApiResults.isEmpty()) {
            throw new NotFoundException(Exceptions.NO_API_RESULTS_FOUND);
        }
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        final User user = userOptional.orElseThrow(() -> new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_F, username)));

        Optional<StockResponseData> stockResponseData = stockResponseDataRepository.findByTicker(dataDTO.getTicker());

        return strategyFactory.getStrategy(stockResponseData)
                .apply( mutableApiResults,
                        user,
                        stockResponseData.orElseGet(()-> StockResponseData.builder().ticker(dataDTO.getTicker()).build()),
                        dataDTO.getStart(),
                        dataDTO.getEnd());
    }


    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        userOptional.orElseThrow(() -> new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_F, username)));
        Optional<StockResponseData> byTicker = stockResponseDataRepository.findByTicker(ticker);
        byTicker.orElseThrow(() -> new NotFoundException(String.format(Exceptions.NO_SAVED_TICKER_FOUND_F, ticker)));

        Long userId = userOptional.get().getId();
        List<Result> resultsByUserAndTicker = resultRepository.findResultsByUserAndTicker(userId, ticker);

        if (resultsByUserAndTicker.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_DATA_FOUND_F, ticker));
        }
        return HistoricalStockPricesData.builder()
                .ticker(byTicker.get().getTicker())
                .results(resultsByUserAndTicker)
                .build();
    }
}
