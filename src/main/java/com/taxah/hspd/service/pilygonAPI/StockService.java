package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.UserResult;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.UserResultRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import com.taxah.hspd.service.auth.impl.UserService;
import com.taxah.hspd.service.pilygonAPI.saveStockDataStrategy.SaveStockDataStrategy;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.taxah.hspd.util.constant.Exceptions.SAVE_STOCK_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    public static final String APPLYING_STRATEGY = "Applying strategy: {}";
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserService userService;
    private final ResultRepository resultRepository;
    private final UserResultRepository userResultRepository;

    @Transactional
    @CacheEvict(value = "hspd", key = "#username + '_' + #tickerName")
    public void saveStockData(SaveStockDataStrategy chosenStrategy,
                              List<Result> apiResults,
                              String username,
                              String tickerName,
                              Optional<StockResponseData> alreadyExist,
                              GetStockResponseDataDTO dataDTO) {
        User existedUser = userService.findByUsernameWithResults(username);
        List<Result> existedInDatabaseResults = findByDateAndTicker(dataDTO);

        List<Result> results = applyStrategy(chosenStrategy,
                apiResults,
                existedInDatabaseResults,
                alreadyExist.orElseGet(() -> StockResponseData.builder().ticker(tickerName).build()),
                dataDTO
        );
        if (!addUserToResults(existedUser, results)) {
            throw new RuntimeException(SAVE_STOCK_FAILED);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Result> applyStrategy(SaveStockDataStrategy strategy,
                                      List<Result> mutableApiResults,
                                      List<Result> existedInDatabaseResults,
                                      StockResponseData stockResponseData,
                                      GetStockResponseDataDTO dataDTO) {
        log.info(APPLYING_STRATEGY, strategy.getClass().getSimpleName());
        return strategy.apply(mutableApiResults, existedInDatabaseResults, stockResponseData, dataDTO.getStart(), dataDTO.getEnd());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean addUserToResults(User user, List<Result> results) {
        List<UserResult> resultsToUpdate = results.stream()
                .filter(result -> !result.contains(user))
                .map(result -> UserResult.builder()
                        .user(user)
                        .result(result)
                        .build())
                .toList();
        if (!resultsToUpdate.isEmpty()) {
            userResultRepository.insertUserResults(resultsToUpdate);
            return true;
        }
        return false;
    }


    @Transactional
    @Cacheable(value = "hspd", key = "#username + '_' + #ticker")
    public HistoricalStockPricesData getSavedInfo(String username, String ticker) {
        StockResponseData stockResponseData = stockResponseDataRepository.findByTicker(ticker).orElseThrow(() ->
                new NotFoundException(String.format(Exceptions.NO_SAVED_TICKER_FOUND_F, ticker))
        );
        User user = (User) userService.loadUserByUsername(username);
        Long userId = user.getId();

        List<Result> resultsByUserAndTicker =
                userResultRepository.findByUserIdAndResultStockResponseDataId(userId, stockResponseData.getId()).stream()
                        .map(UserResult::getResult)
                        .collect(Collectors.toList());

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

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Result> findByDateAndTicker(GetStockResponseDataDTO dataDTO) {
        return resultRepository.findByDateAndTicker(dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd());
    }
}
