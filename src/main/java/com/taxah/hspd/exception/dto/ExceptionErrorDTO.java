package com.taxah.hspd.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO со строковым описанием ошибки и строковым Trace")
public class ExceptionErrorDTO {
    @Schema(description = "UUID ошибки")
    private final UUID errorUUID;
    @Schema(description = "Сообщение ошибки", example = "Строковое сообщение об ошибке")
    private String errorMessage;
    @Schema(description = "Error Stack trace")
    private final String trace;
}
