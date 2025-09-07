package com.nextjstemplate.config;

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
                    .title("Nextjs Template Boot API")
                    .description("API Documentation for all REST endpoints")
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
                    .description("Enter 'Bearer <JWT token>' to authenticate.")
            );
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("all-apis").packagesToScan("com.nextjstemplate.web.rest").pathsToMatch("/**").build();
    }
}
