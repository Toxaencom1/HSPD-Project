package com.taxah.hspd.service.polygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.UserResult;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.UserResultRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.repository.polygonAPI.StockResponseDataRepository;
import com.taxah.hspd.service.auth.impl.UserService;
import com.taxah.hspd.service.polygonAPI.saveStockDataStrategy.SaveStockDataStrategy;
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

import static com.taxah.hspd.util.constant.Params.APPLYING_STRATEGY;


@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockResponseDataRepository stockResponseDataRepository;
    private final UserService userService;
    private final ResultRepository resultRepository;
    private final UserResultRepository userResultRepository;

    @Transactional
    @CacheEvict(value = "hspd", key = "#username.toLowerCase() + '_' + #tickerName")
    public void saveStockData(SaveStockDataStrategy chosenStrategy,
                              List<Result> apiResults,
                              String username,
                              String tickerName,
                              Optional<StockResponseData> alreadyExistTicker,
                              GetStockResponseDataDTO dataDTO) {
        User existedUser = userService.findByUsernameWithResults(username);
        List<Result> existedInDatabaseResults = findByDateAndTicker(dataDTO);

        log.info(APPLYING_STRATEGY, chosenStrategy.getClass().getSimpleName());

        List<Result> results = chosenStrategy.apply(
                apiResults,
                existedInDatabaseResults,
                alreadyExistTicker.orElseGet(() -> StockResponseData.builder().ticker(tickerName).build())
        );
        if (!addUserToResults(existedUser, results)) {
            throw new AlreadyExistsException(String.format(Exceptions.DATA_ALREADY_EXISTS_F, tickerName));
        }
    }

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
    @Cacheable(value = "hspd", key = "#username.toLowerCase() + '_' + #ticker")
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
