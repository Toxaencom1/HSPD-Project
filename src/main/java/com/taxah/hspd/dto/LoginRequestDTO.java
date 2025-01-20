package com.taxah.hspd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static com.taxah.hspd.util.constant.Validations.*;

@Data
@Builder
@Schema(description = "DTO для входа в приложение.")
public class LoginRequestDTO {
    @Schema(description = "Строка имени пользователя.", example = "TaXaH")
    @Pattern(
            regexp = USERNAME_REGEX,
            message = USERNAME_VALIDATION
    )
    @NotBlank(message = USERNAME_CANNOT_BE_EMPTY)
    private String username;

    @Schema(description = "Строка пароля пользователя.", example = "***-masked-***")
    @Size(
            min = PASSWORD_MIN_LENGTH,
            max = PASSWORD_MAX_LENGTH,
            message = PASSWORD_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = PASSWORD_CANNOT_BE_BLANK)
    private String password;
}
