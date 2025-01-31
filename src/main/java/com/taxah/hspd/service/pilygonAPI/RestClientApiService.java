package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.polygon.TickerResponseData;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.taxah.hspd.util.constant.Exceptions.*;
import static com.taxah.hspd.util.constant.Params.*;

@Primary
@Data
@Service
@RequiredArgsConstructor
@Slf4j
public class RestClientApiService implements ApiService {
    private final RestClient restClient;

    @Value("${polygonAPI.url.main}")
    private String mainUrl;
    @Value("${polygonAPI.apiKey}")
    private String apiKey;
    @Value("${polygonAPI.url.ticker.details}")
    private String tickerDetailsUrl;

    @Override
    public List<Result> getNewApiResults(GetStockResponseDataDTO dataDTO) {
        String uriString = UriComponentsBuilder
                .fromUri(URI.create(String.format(mainUrl, dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd())))
                .queryParam(ADJUSTED, ADJUSTED_BOOLEAN)
                .queryParam(SORT, SORT_TYPE)
                .queryParam(API_KEY, apiKey)
                .toUriString();
        log.info(uriString);
        List<Result> apiResults;
        try {
            apiResults = Objects.requireNonNull(restClient.get()
                    .uri(uriString)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(StockResponseData.class), POLYGON_IS_UNREACHABLE).getResults();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(String.format(NO_API_RESULTS_FOUND_F, dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd()));
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthorizationDeniedException(CHECK_YOUR_API_PLAN);
            }
            throw e;
        }
        if (apiResults.isEmpty()) {
            throw new NotFoundException(
                    String.format(NO_DATA_FOUND_IN_POLYGON_FOR_PERIOD_F,
                            dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd())
            );
        }
        return apiResults;
    }

    @Override
    public TickerResponseData getTickerDetails(String ticker) {
        String uriString = UriComponentsBuilder.fromUri(URI.create(String.format(tickerDetailsUrl, ticker)))
                .queryParam(API_KEY, apiKey)
                .toUriString();
        try {
            return restClient.get()
                    .uri(uriString)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(TickerResponseData.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(String.format(TICKER_NOT_FOUND_F, ticker));
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthorizationDeniedException(AUTHORIZATION_DENIED);
            }
            throw e;
        }
    }
}
