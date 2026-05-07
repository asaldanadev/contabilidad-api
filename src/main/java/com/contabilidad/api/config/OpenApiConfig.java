package com.contabilidad.api.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sistema Contable")
                        .description("API REST para gestión contable con partida doble. " +
                                "Desarrollado con Java 17 + Spring Boot 3 + PostgreSQL.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@email.com")));
    }
}
