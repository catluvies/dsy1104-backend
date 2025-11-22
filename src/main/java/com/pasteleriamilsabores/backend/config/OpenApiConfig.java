package com.pasteleriamilsabores.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Pastelería Mil Sabores")
                        .version("1.0")
                        .description("API REST para la gestión de productos y pedidos de Pastelería Mil Sabores")
                        .contact(new Contact()
                                .name("Pastelería Mil Sabores")
                                .email("contacto@milsabores.cl")));
    }
}
