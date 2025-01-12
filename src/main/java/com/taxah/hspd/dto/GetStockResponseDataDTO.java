package com.taxah.hspd.dto;

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
    @NotNull(message = "Ticker не может быть null")
    @NotBlank(message = "Ticker не может быть пустым")
    private String ticker;

    @NotNull(message = "start не может быть null")
    @PastOrPresent(message = "Start должен быть не позже настоящего времени")
    private LocalDate start;

    @NotNull(message = "end не может быть null")
    @PastOrPresent(message = "Start должен быть не позже настоящего времени")
    private LocalDate end;

    @AssertTrue(message = "start должен быть раньше 'end' даты")
    public boolean isStartBeforeEnd() {
        if (start == null || end == null) {
            return true;
        }
        return start.isBefore(end) || start.isEqual(end);
    }
}
