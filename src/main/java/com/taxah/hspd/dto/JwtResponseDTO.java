package com.taxah.hspd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для возврата данных о Token авторизации.")
public class JwtResponseDTO {
    @Schema(description = "Строка Access токена.", example = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJpZCI6Miwicm9sZXMiOiJBRE1JTiJ9XSwiaWQiOjIsImVtYWlsIjoiQWRtaW5AbWFpbC50ZXN0IiwiYXV0aG9yaXRpZXMiOlt7ImlkIjoxLCJuYW1lIjoiZ2V0X3VzZXJfcGVybWlzc2lvbiIsImF1dGhvcml0eSI6ImdldF91c2VyX3Blcm1pc3Npb24ifSx7ImlkIjoyLCJuYW1lIjoicG9zdF91c2VyX3Blcm1pc3Npb24iLCJhdXRob3JpdHkiOiJwb3N0X3VzZXJfcGVybWlzc2lvbiJ9XSwic3ViIjoiQWRtaW4iLCJpYXQiOjE3MzY5NDM3NTMsImV4cCI6MTczNzAzMDE1M30.-pPi1JMYwI4KfScBpP_PoBF_y4lAOES2KI9HlnHFjiI")
    private String token;
    @Schema(description = "Строка Refresh токена.", example = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJpZCI6Miwicm9sZXMiOiJBRE1JTiJ9XSwiaWQiOjIsImVtYWlsIjoiQWRtaW5AbWFpbC50ZXN0IiwiYXV0aG9yaXRpZXMiOlt7ImlkIjoxLCJuYW1lIjoiZ2V0X3VzZXJfcGVybWlzc2lvbiIsImF1dGhvcml0eSI6ImdldF91c2VyX3Blcm1pc3Npb24ifSx7ImlkIjoyLCJuYW1lIjoicG9zdF91c2VyX3Blcm1pc3Npb24iLCJhdXRob3JpdHkiOiJwb3N0X3VzZXJfcGVybWlzc2lvbiJ9XSwic3ViIjoiQWRtaW4iLCJpYXQiOjE3MzY5NDM3NTMsImV4cCI6MTczNzAzMDE1M30.-pPi1JMYwI4KfScBpP_PoBF_y4lAOES2KI9HlnHFjiI")
    private String refreshToken;
}
