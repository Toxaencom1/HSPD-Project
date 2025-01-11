package com.taxah.hspd.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GetStockResponseDataDTO {
    String ticker;
    LocalDate start;
    LocalDate end;
}
