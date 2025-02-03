package com.taxah.hspd.service.polygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.polygon.TickerResponseData;
import com.taxah.hspd.entity.polygonAPI.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.taxah.hspd.util.constant.Params.TICKER_STATUS_OK;


@Service
@RequiredArgsConstructor
public class PolygonService {

    private final ApiService apiService;

    public boolean checkTickerInPolygon(String ticker) {
        TickerResponseData tickerDetails = apiService.getTickerDetails(ticker);
        return tickerDetails.getStatus().equals(TICKER_STATUS_OK);
    }

    public List<Result> getNewApiResults(GetStockResponseDataDTO dataDTO) {
        return apiService.getNewApiResults(dataDTO);
    }

}
