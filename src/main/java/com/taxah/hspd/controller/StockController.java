package com.taxah.hspd.controller;

import com.taxah.hspd.controller.doc.StockControllerSwagger;
import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.pilygonAPI.StockService;
import com.taxah.hspd.util.constant.Endpoints;
import com.taxah.hspd.util.constant.Exceptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.API_USER)
@RequiredArgsConstructor
public class StockController implements StockControllerSwagger {
    private final StockService stockService;

    @PostMapping(Endpoints.SAVE)
    public ResponseEntity<Void> saveStocks(@RequestBody @Valid GetStockResponseDataDTO dataDTO) {
        String username = getAuthenticationUsername();

        stockService.saveStockData(username, dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(Endpoints.SAVED)
    public ResponseEntity<HistoricalStockPricesData> getSavedInfoByTicker(@RequestParam String ticker) {
        String username = getAuthenticationUsername();

        HistoricalStockPricesData savedInfo = stockService.getSavedInfo(username, ticker);

        return ResponseEntity.status(HttpStatus.OK).body(savedInfo);
    }

    private static String getAuthenticationUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException(Exceptions.USER_IS_NOT_AUTHENTICATED);
        }
        return authentication.getName();
    }
}
