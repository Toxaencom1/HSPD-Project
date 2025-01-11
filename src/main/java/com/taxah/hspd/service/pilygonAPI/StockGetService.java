package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
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
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class StockGetService {
    @Value("${polygonAPI.url}")
    private String url;
    @Value("${polygonAPI.apiKey}")
    private String apiKey;

    private final RestTemplate template;

    public StockResponseData getData(String ticker, LocalDate dateFrom, LocalDate dateTo) {
        String from = dateFormating(dateFrom);
        String to = dateFormating(dateTo);
        String uriString = UriComponentsBuilder.fromUri(URI.create(String.format(url, ticker,from,to)))
                .queryParam("adjusted", "true")
                .queryParam("sort", "asc")
                .queryParam("apiKey", apiKey)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/*json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<StockResponseData> response = template.exchange(
                uriString,                       // URL запроса
                HttpMethod.GET,            // HTTP метод
                entity,                    // Заголовки запроса
                StockResponseData.class  // Класс ожидаемого ответа
        );

        return response.getBody();
    }

    private String dateFormating(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
