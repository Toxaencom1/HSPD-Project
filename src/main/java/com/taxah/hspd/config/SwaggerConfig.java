package com.taxah.hspd.config;

import com.taxah.hspd.util.constant.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = Constants.APP_NAME,
                description = "API для хранения исторических данных по акциям.",
                version = "0.0.1",
                contact = @Contact(
                        name = "TaXaH",
                        email = "Toxaencom@mail.ru"
                )
        )
)
public class SwaggerConfig {

}

