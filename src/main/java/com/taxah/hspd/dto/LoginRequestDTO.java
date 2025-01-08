package com.taxah.hspd.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;
    @Size(min = 4, max = 255, message = "Длина пароля должна быть от 4 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
