package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.utils.DateTimeCustomFormatter;
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

@Service
@RequiredArgsConstructor
public class TemplateAPIService {
    public static final String ADJUSTED = "adjusted";
    public static final String SORT = "sort";
    public static final String API_KEY = "apiKey";
    public static final String TRUE = "true";
    public static final String ASC = "asc";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/*json";

    @Value("${polygonAPI.url}")
    private String url;
    @Value("${polygonAPI.apiKey}")
    private String apiKey;

    private final RestTemplate template;
    private final DateTimeCustomFormatter formatter;

    public StockResponseData getData(String ticker, LocalDate dateFrom, LocalDate dateTo) {
        String from = formatter.format(dateFrom);
        String to = formatter.format(dateTo);
        String uriString = UriComponentsBuilder.fromUri(URI.create(String.format(url, ticker, from, to)))
                .queryParam(ADJUSTED, TRUE)
                .queryParam(SORT, ASC)
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


}
