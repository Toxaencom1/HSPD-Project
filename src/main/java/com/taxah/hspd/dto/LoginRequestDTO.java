package com.taxah.hspd.dto;

import com.taxah.hspd.util.constant.Validations;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @Size(
            min = Validations.USERNAME_MIN_LENGTH,
            max = Validations.USERNAME_MAX_LENGTH,
            message = Validations.USERNAME_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validations.USERNAME_CANNOT_BE_EMPTY)
    private String username;
    @Size(
            min = Validations.PASSWORD_MIN_LENGTH,
            max = Validations.PASSWORD_MAX_LENGTH,
            message = Validations.PASSWORD_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validations.PASSWORD_CANNOT_BE_BLANK)
    private String password;
}
