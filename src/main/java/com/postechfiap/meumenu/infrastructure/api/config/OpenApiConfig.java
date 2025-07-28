package com.postechfiap.meumenu.infrastructure.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth",
//                                new SecurityScheme()
//                                        .name("Authorization")
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")))
                .info(
                        new Info().title("Meu Menu API")
                                .description("Meu Menu - API para Restaurantes e Clientes")
                                .version("v0.0.1")
                                .license(new License().name("FIAP").url("https://github.com/JavaTeamPosTech/Piloto"))
                );
    }
}