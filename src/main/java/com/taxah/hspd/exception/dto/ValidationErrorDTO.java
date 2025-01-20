package com.taxah.hspd.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO со списком ошибок валидации")
public class ValidationErrorDTO {
    @Schema(description = "UUID ошибки")
    private final UUID errorUUID;
    @Schema(description = "Сообщения ошибок", example = "Список ошибок валидации")
    private final List<String> errors;
}
