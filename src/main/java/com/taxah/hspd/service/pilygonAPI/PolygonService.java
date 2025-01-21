package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.polygon.TickerResponseData;
import com.taxah.hspd.entity.polygonAPI.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolygonService {
    public static final String TICKER_STATUS_OK = "OK";

    private final TemplateAPIService templateAPIService;

    public boolean checkTickerInPolygon(String ticker) {
        TickerResponseData tickerDetails = templateAPIService.getTickerDetails(ticker);
        return tickerDetails.getStatus().equals(TICKER_STATUS_OK);
    }

    public List<Result> getNewApiResults(GetStockResponseDataDTO dataDTO) {
        return templateAPIService.getNewApiResults(dataDTO);
    }

}
