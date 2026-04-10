package com.Jakima_Estate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI jakimaOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token. Example: eyJhbGciOiJIUzUxMiJ9...");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Jakima Estate API")
                        .description("Backend API for Jakima Properties - Nairobi's Premier Real Estate Agency. Supports JPEG, PNG, GIF, WEBP, BMP, TIFF, HEIC, AVIF image uploads and MP4/MOV/AVI video uploads up to 50MB.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jakima Properties")
                                .email("info@jakimaestate.com")
                                .url("https://jakima-estate-flow.base44.app"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme)
                        .addSchemas("FileUpload", new Schema<>()
                                .type("object")
                                .addProperty("file", new StringSchema().format("binary"))))
                .addSecurityItem(securityRequirement);
    }
}