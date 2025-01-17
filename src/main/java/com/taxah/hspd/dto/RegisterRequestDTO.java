package com.taxah.hspd.dto;

import com.taxah.hspd.util.constant.Validations;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO для регистрации в приложении.")
public class RegisterRequestDTO {
    @Schema(description = "Строка имени пользователя.", example = "TaXaH")
    @Pattern(
            regexp = Validations.USERNAME_REGEX,
            message = Validations.USERNAME_VALIDATION
    )
    @Size(
            min = Validations.USERNAME_MIN_LENGTH,
            max = Validations.USERNAME_MAX_LENGTH,
            message = Validations.USERNAME_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validations.USERNAME_CANNOT_BE_EMPTY)
    private String username;

    @Schema(description = "Строка пароля пользователя.", example = "***-masked-***")
    @Size(
            min = Validations.PASSWORD_MIN_LENGTH,
            max = Validations.PASSWORD_MAX_LENGTH,
            message = Validations.PASSWORD_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validations.PASSWORD_CANNOT_BE_BLANK)
    private String password;

    @Schema(description = "Строка Email пользователя.", example = "semple@mail.com")
    @Size(
            min = Validations.EMAIL_MIN_LENGTH,
            max = Validations.EMAIL_MAX_LENGTH,
            message = Validations.EMAIL_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validations.EMAIL_ADDRESS_CANNOT_BE_EMPTY)
    @Email(message = Validations.EMAIL_ADDRESS_MUST_BE_IN_FORMAT)
    private String email;
}
