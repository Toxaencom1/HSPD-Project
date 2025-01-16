package com.taxah.hspd.dto;

import com.taxah.hspd.util.constant.Validations;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "DTO для передачи данных об акциях.")
public class GetStockResponseDataDTO {

    @Schema(description = "Уникальный идентификатор акции.", example = "AAPL")
    @NotNull(message = Validations.TICKER_CANNOT_BE_NULL)
    @NotBlank(message = Validations.TICKER_CANNOT_BE_EMPTY)
    private String ticker;

    @Schema(description = "Дата начала для поиска.", example = "2025-01-16")
    @NotNull(message = Validations.START_CANNOT_BE_NULL)
    @PastOrPresent(message = Validations.START_MUST_BE_NOT_LATER_THAN_PRESENT_TIME)
    private LocalDate start;

    @Schema(description = "Дата конца для поиска.", example = "2025-01-16")
    @NotNull(message = Validations.END_CANNOT_BE_NULL)
    @PastOrPresent(message = Validations.END_MUST_BE_NOT_LATER_THAN_PRESENT_TIME)
    private LocalDate end;

    @AssertTrue(message = Validations.START_MUST_BE_EARLIER_END_DATE)
    public boolean isStartBeforeEnd() {
        if (start == null || end == null) {
            return true;
        }
        return start.isBefore(end) || start.isEqual(end);
    }
}
