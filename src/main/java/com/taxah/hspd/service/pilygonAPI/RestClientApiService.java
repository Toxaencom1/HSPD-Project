package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.polygon.TickerResponseData;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
import java.util.function.Supplier;

import static com.taxah.hspd.util.constant.Exceptions.*;
import static com.taxah.hspd.util.constant.Params.*;

@Primary
@Data
@Service
@RequiredArgsConstructor
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
        StockResponseData response = executeRequest(() -> restClient.get()
                        .uri(uriString)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(StockResponseData.class),
                String.format(NO_API_RESULTS_FOUND_F, dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd()),
                CHECK_YOUR_API_PLAN
        );

        if (response == null || response.getResults() == null) {
            throw new RuntimeException(POLYGON_IS_UNREACHABLE);
        }
        List<Result> apiResults = response.getResults();

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

        return executeRequest(() -> restClient.get()
                        .uri(uriString)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(TickerResponseData.class),
                String.format(TICKER_NOT_FOUND_F, ticker),
                AUTHORIZATION_DENIED
        );
    }

    private <T> T executeRequest(Supplier<T> request, String notFoundMessage, String authorizationDeniedMessage) {
        try {
            return request.get();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(notFoundMessage);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthorizationDeniedException(authorizationDeniedMessage);
            }
            throw e;
        }
    }
}
