package com.taxah.hspd.service.pilygonAPI;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.polygon.TickerResponseData;
import com.taxah.hspd.entity.polygonAPI.Result;

import java.util.List;

public interface ApiService {

    List<Result> getNewApiResults(GetStockResponseDataDTO dataDTO);

    TickerResponseData getTickerDetails(String ticker);
}
