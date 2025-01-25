package com.taxah.hspd.controller.doc;

import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.dto.HistoricalStockPricesData;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import com.taxah.hspd.exception.dto.ValidationErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Работа с данными", description = "API для сохранения и просмотра данных.")
public interface StockControllerSwagger {

    @Operation(
            description = "Сохраняет данные о акциях в приложении.",
            summary = "- Сохранение",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные успешно сохранены."),
                    @ApiResponse(
                            description = "Ошибка валидации данных, переданных пользователем.",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Доступ к ресурсу запрещен.",
                            responseCode = "403",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Данные уже существуют в базе данных.",
                            responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Произошла внутренняя ошибка сервера.",
                            responseCode = "500",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    )
            }
    )
    ResponseEntity<Void> saveStocks(GetStockResponseDataDTO dataDTO);

    @Operation(
            description = "Получает сохраненную информацию о ценах акций по тикеру.",
            summary = "- Получение",
            parameters = {
                    @Parameter(
                            name = "ticker",
                            description = "Уникальный идентификатор акции.",
                            example = "AAPL",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно получена.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = HistoricalStockPricesData.class))
                    ),
                    @ApiResponse(
                            description = "Акции с указанным тикером не найдены.",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка валидации данных, переданных пользователем.",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ValidationErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Доступ к ресурсу запрещен.",
                            responseCode = "403",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Произошла внутренняя ошибка сервера.",
                            responseCode = "500",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    )
            }
    )
    ResponseEntity<HistoricalStockPricesData> getSavedInfoByTicker(String ticker);
}
