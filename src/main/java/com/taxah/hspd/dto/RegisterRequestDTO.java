package com.taxah.hspd.dto;

import com.taxah.hspd.utils.constant.Validation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
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

    @Size(
            min = Validation.EMAIL_MIN_LENGTH,
            max = Validation.EMAIL_MAX_LENGTH,
            message = Validation.EMAIL_MUST_CONTAIN_CHARACTERS
    )
    @NotBlank(message = Validation.EMAIL_ADDRESS_CANNOT_BE_EMPTY)
    @Email(message = Validation.EMAIL_ADDRESS_MUST_BE_IN_FORMAT)
    private String email;
}
