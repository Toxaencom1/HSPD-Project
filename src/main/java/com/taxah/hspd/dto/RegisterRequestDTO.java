package com.taxah.hspd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static com.taxah.hspd.util.constant.Validations.*;

@Data
@Builder
@Schema(description = "DTO для регистрации в приложении.")
public class RegisterRequestDTO {
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

    @Schema(description = "Строка Email пользователя.", example = "semple@mail.com")
    @Size(
            min = EMAIL_MIN_LENGTH,
            max = EMAIL_MAX_LENGTH,
            message = EMAIL_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = EMAIL_ADDRESS_CANNOT_BE_EMPTY)
    @Email(message = EMAIL_ADDRESS_MUST_BE_IN_FORMAT)
    private String email;
}
