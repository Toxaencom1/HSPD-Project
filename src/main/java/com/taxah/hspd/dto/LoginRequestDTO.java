package com.taxah.hspd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;
    @Size(min = 4, max = 255, message = "Длина пароля должна быть от 4 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
