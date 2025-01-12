package com.taxah.hspd.controller;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.pilygonAPI.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/save")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockResponseData> saveStocks(@RequestBody @Valid GetStockResponseDataDTO dataDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String username = authentication.getName();

        stockService.saveStockData(username,dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd());

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
