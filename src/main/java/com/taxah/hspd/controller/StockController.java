package com.taxah.hspd.controller;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.pilygonAPI.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping("/save")
    public ResponseEntity<StockResponseData> saveStocks(@RequestBody @Valid GetStockResponseDataDTO dataDTO) {
        String username = getAuthenticationUsername();

        stockService.saveStockData(username,dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd());

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/saved")
    public ResponseEntity<HistoricalStockPricesData> getSavedInfoByTicker(@RequestParam String ticker) {
        String username = getAuthenticationUsername();

        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.getSavedInfo(username, ticker));
    }

    private static String getAuthenticationUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        return authentication.getName();
    }
}
