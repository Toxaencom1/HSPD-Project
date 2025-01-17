package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateAPIService {
    public static final String ADJUSTED = "adjusted";
    public static final String SORT = "sort";
    public static final String API_KEY = "apiKey";
    public static final String ADJUSTED_BOOLEAN = "true";
    public static final String SORT_TYPE = "asc";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/*json";

    @Value("${polygonAPI.url}")
    private String url;
    @Value("${polygonAPI.apiKey}")
    private String apiKey;

    private final RestTemplate template;

    public StockResponseData getData(String ticker, LocalDate dateFrom, LocalDate dateTo) {
        String uriString = UriComponentsBuilder.fromUri(URI.create(String.format(url, ticker, dateFrom, dateTo)))
                .queryParam(ADJUSTED, ADJUSTED_BOOLEAN)
                .queryParam(SORT, SORT_TYPE)
                .queryParam(API_KEY, apiKey)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCEPT, APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<StockResponseData> response = template.exchange(
                uriString,
                HttpMethod.GET,
                entity,
                StockResponseData.class
        );

        return response.getBody();
    }

    public List<Result> getNewApiResults(GetStockResponseDataDTO dataDTO) {
        List<Result> apiResults = getData(dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd()).getResults();
        if (apiResults.isEmpty()) {
            throw new NotFoundException(String.format(Exceptions.NO_TICKER_FOUND_F, dataDTO.getTicker()));
        }
        return apiResults;
    }
}
