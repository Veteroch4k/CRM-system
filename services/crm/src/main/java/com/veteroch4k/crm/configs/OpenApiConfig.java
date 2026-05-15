package com.veteroch4k.crm.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
        title = "CRM-system API",
        description = "API CRM-системы",
        version = "1.0",
        contact = @Contact(
            name = "Popov Victor",
            email = "viktor.popov2005@mail.ru",
            url = "https://github.com/Veteroch4k"
        )
    )
)
public class OpenApiConfig {
}
