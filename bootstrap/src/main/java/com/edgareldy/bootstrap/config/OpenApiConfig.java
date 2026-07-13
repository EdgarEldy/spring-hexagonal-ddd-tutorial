package com.edgareldy.bootstrap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc-openapi title/description/version, surfaced in Swagger UI.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hexagonalDddTutorialOpenApi() {
        return new OpenAPI().info(new Info().title("Spring Hexagonal DDD Tutorial")
                .description("Hexagonal architecture (ports and adapters) combined with Domain-Driven Design.")
                .version("v1"));
    }
}
