package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String APP_NAME = "hspd";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String INFO = """
                Информация для тестирования приложения,
                ниже описаны скрипты для наполнения базы данных приложения
                стандартными ролями, допусками и пользователями.
                Пароли зашифрованы bcrypt(12), соответствуют имени пользователей.
                
                INSERT INTO public.roles (id, roles)
                VALUES (1, 'USER'),
                       (2, 'ADMIN');
                
                INSERT INTO public.permission (id, name)
                VALUES (1, 'user_permission'),
                       (2, 'admin_permission');
                
                INSERT INTO public.permission_roles (permissions_id, roles_id)
                VALUES (1, 1),
                       (1, 2),
                       (2, 2);
                
                INSERT INTO users (id, password, username, email)
                VALUES (1, '$2a$12$1ZlddGolw1oMtHoPt5KgM./xZRGdLNjSqS6wyuk6lTOHQc0c.5b1O', 'test', 'test@mail.test'),
                       (2, '$2a$12$TyYqv1S/i35etl2lFvKmFOnGQR.6moeJFUcwFPUF3sJe9G93SywRO', 'admin', 'Admin@mail.test');
                
                INSERT INTO users_roles (roles_id, user_id)
                VALUES (1, 1),
                       (2, 2);
                
                Объекты для тестов:
                    Регистрация:
                    {
                        "username": "{{testUsername}}",
                        "email":"{{testUsername}}@mail.test",
                        "password": "{{testPassword}}"
                    }
                    Логин:
                    {
                        "username": "{{testUsername}}",
                        "password": "{{testPassword}}"
                    }
                    Сохранение:
                    {
                        "ticker": "AAPL",
                        "start": "2023-01-01",
                        "end": "2023-02-01"
                    }
                    Получение:
                        параметр: AAPL
                """;
}
