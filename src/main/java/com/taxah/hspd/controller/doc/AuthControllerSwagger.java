package com.taxah.hspd.controller.doc;

import com.taxah.hspd.dto.JwtRequestDTO;
import com.taxah.hspd.dto.JwtResponseDTO;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Вход/Регистрация в приложение", description = "API для регистрации и входа в приложение.")
public interface AuthControllerSwagger {

    @Operation(
            description = "Endpoint для регистрации пользователя в приложении.",
            summary = "- Регистрация",
            responses = {
                    @ApiResponse(description = "Регистрация успешно завершена", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = JwtResponseDTO.class))),
                    @ApiResponse(description = "Строковое сообщение об ошибке валидации данных переданных пользователем.",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))),
                    @ApiResponse(
                            description = "Произошла внутренняя ошибка сервера.",
                            responseCode = "500",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    )
            }
    )
    ResponseEntity<JwtResponseDTO> signUp(RegisterRequestDTO request);

    @Operation(
            description = "Endpoint для идентификации пользователя в приложении.",
            summary = "- Вход",
            responses = {
                    @ApiResponse(description = "Вход успешно выполнен",responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = JwtResponseDTO.class))),
                    @ApiResponse(description = "Строковое сообщение об ошибке валидации данных переданных пользователем.",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))),
                    @ApiResponse(description = "Строковое сообщение об ошибке, указывающей, что пользователь с переданными данными не найден.",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))),
                    @ApiResponse(
                            description = "Произошла внутренняя ошибка сервера.",
                            responseCode = "500",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    )
            }
    )
    ResponseEntity<JwtResponseDTO> signIn(LoginRequestDTO request);

    @Operation(
            description = "Endpoint для обновления Токен Access по Токен Refresh.",
            summary = "- Обновление Токена доступа",
            responses = {
                    @ApiResponse(description = "Обновление успешно выполнено",responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponseDTO.class))),
                    @ApiResponse(description = "Строковое сообщение об ошибке, указывающей, что Токен обновления не найден в базе валидных токенов.",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))),
                    @ApiResponse(description = "Строковое сообщение об ошибке, указывающей, что Токен обновления просрочен.",
                            responseCode = "403",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))),
                    @ApiResponse(
                            description = "Произошла внутренняя ошибка сервера.",
                            responseCode = "500",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StringErrorDTO.class))
                    )
            }
    )
    ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody JwtRequestDTO requestDTO);
}
