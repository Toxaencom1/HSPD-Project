package com.taxah.hspd.controller;

import com.taxah.hspd.controller.doc.StockControllerSwagger;
import com.taxah.hspd.controller.handler.UserAccessHandler;
import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.service.pilygonAPI.StockService;
import com.taxah.hspd.service.pilygonAPI.TemplateAPIService;
import com.taxah.hspd.util.constant.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.API_DATA)
@RequiredArgsConstructor
public class StockController implements StockControllerSwagger, UserAccessHandler {
    private final StockService stockService;
    private final TemplateAPIService templateAPIService;

    @PostMapping(Endpoints.SAVE)
    public ResponseEntity<Void> saveStocks(@RequestBody @Valid GetStockResponseDataDTO dataDTO) {
        String username = getAuthenticationUsername();

        List<Result> results = templateAPIService.getNewApiResults(dataDTO);
        stockService.saveStockData(results, username, dataDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(Endpoints.FETCH)
    public ResponseEntity<HistoricalStockPricesData> getSavedInfoByTicker(@RequestParam String ticker) {
        String username = getAuthenticationUsername();

        HistoricalStockPricesData savedInfo = stockService.getSavedInfo(username, ticker);

        return ResponseEntity.status(HttpStatus.OK).body(savedInfo);
    }
}
