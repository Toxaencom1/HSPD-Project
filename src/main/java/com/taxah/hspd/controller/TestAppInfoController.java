package com.taxah.hspd.controller;

import com.taxah.hspd.util.constant.Constants;
import com.taxah.hspd.util.constant.Endpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.TEST_INFO_ENDPOINT)
@Tag(name = "Информация", description = "API для получения информации")
public class TestAppInfoController {
    @Operation(
            summary = "Получить тестовые SQL-скрипты",
            description = "Этот endpoint возвращает информацию в виде текста, тестовые SQL-скрипты для локального тестирования приложения.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "SQL-скрипты", content = @Content(mediaType = "text/plain"))
            }
    )
    @GetMapping
    public ResponseEntity<String> testAppInfo() {
        return ResponseEntity.ok(Constants.INFO);
    }
}
