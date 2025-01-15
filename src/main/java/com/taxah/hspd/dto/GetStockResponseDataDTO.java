package com.taxah.hspd.dto;

import com.taxah.hspd.utils.constant.Validation;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GetStockResponseDataDTO {


    @NotNull(message = Validation.TICKER_CANNOT_BE_NULL)
    @NotBlank(message = Validation.TICKER_CANNOT_BE_EMPTY)
    private String ticker;

    @NotNull(message = Validation.START_CANNOT_BE_NULL)
    @PastOrPresent(message = Validation.START_MUST_BE_NOT_LATER_THAN_PRESENT_TIME)
    private LocalDate start;

    @NotNull(message = Validation.END_CANNOT_BE_NULL)
    @PastOrPresent(message = Validation.END_MUST_BE_NOT_LATER_THAN_PRESENT_TIME)
    private LocalDate end;

    @AssertTrue(message = Validation.START_MUST_BE_EARLIER_END_DATE)
    public boolean isStartBeforeEnd() {
        if (start == null || end == null) {
            return true;
        }
        return start.isBefore(end) || start.isEqual(end);
    }
}
