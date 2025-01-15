package com.taxah.hspd.dto;

import com.taxah.hspd.utils.constant.Validation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @Size(
            min = Validation.USERNAME_MIN_LENGTH,
            max = Validation.USERNAME_MAX_LENGTH,
            message = Validation.USERNAME_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validation.USERNAME_CANNOT_BE_EMPTY)
    private String username;
    @Size(
            min = Validation.PASSWORD_MIN_LENGTH,
            max = Validation.PASSWORD_MAX_LENGTH,
            message = Validation.PASSWORD_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validation.PASSWORD_CANNOT_BE_BLANK)
    private String password;
}
