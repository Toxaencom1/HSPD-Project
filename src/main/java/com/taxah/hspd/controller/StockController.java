package com.taxah.hspd.controller;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.service.pilygonAPI.StockService;
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
//    private final StockService stockService;
    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockResponseData> saveStocks(@RequestBody GetStockResponseDataDTO dataDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String username = authentication.getName();

        System.out.println("Username = "+username);

        StockResponseData stock = stockService.saveStockData(dataDTO.getTicker(), dataDTO.getStart(), dataDTO.getEnd());

//        UserStocksEntry entry = UserStocksEntry.builder()
//                .username(username)
//                .stockData(new ArrayList<>())
//                .build();
//
//        entry.addStockResponseData(stock);
//        UserStocksEntry save = userStocksEntryRepository.save(entry);
//        System.out.println("Stock = "+save);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }
}
