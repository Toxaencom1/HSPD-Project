package com.taxah.hspd.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO со строковым описанием ошибки")
public class StringErrorDTO {
    @Schema(description = "UUID ошибки")
    private final UUID errorUUID;
    @Schema(description = "Сообщение ошибки", example = "Строковое сообщение об ошибке")
    private String errorMessage;
}
