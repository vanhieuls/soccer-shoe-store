package com.dailycodework.shopping_cart.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.service.title}") String title,
            @Value("${open.api.service.version}") String version,
            @Value("${open.api.service.description}") String description,
            @Value("${open.api.service.serverUrl}") String serverUrl,
            @Value("${open.api.service.serverName}") String serverName) {

        return new OpenAPI()
                // Builder style
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .servers(List.of(new Server().url(serverUrl).description(serverName)))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }
}
