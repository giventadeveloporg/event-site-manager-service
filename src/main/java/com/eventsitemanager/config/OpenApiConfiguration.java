package com.eventsitemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("Event Site Manager Service API")
                    .description("API Documentation for all REST endpoints including Clerk Authentication, Admin Management, and Webhooks")
                    .version("1.0")
                    .license(new License().name("Unlicensed"))
            )
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
            .schemaRequirement(
                "bearer-jwt",
                new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER)
                    .description("JWT token obtained from /api/auth/sign-in or /api/auth/sign-in/social. Format: Bearer <token>")
            );
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("all-apis").packagesToScan("com.eventsitemanager.web.rest").pathsToMatch("/**").build();
    }

    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi
            .builder()
            .group("authentication")
            .packagesToScan("com.eventsitemanager.web.rest")
            .pathsToMatch("/api/auth/**")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi
            .builder()
            .group("admin")
            .packagesToScan("com.eventsitemanager.web.rest")
            .pathsToMatch("/api/admin/**")
            .build();
    }

    @Bean
    public GroupedOpenApi webhooksApi() {
        return GroupedOpenApi
            .builder()
            .group("webhooks")
            .packagesToScan("com.eventsitemanager.web.rest")
            .pathsToMatch("/api/webhooks/**")
            .build();
    }
}
