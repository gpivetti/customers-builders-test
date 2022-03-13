package br.com.builders.customer.main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("[Builders Test] Customer Service - Gabriel Pivetti")
                .description("Customer Service Endpoints, developed for Builders Test Evaluation." +
                        "<br><br><strong>Author</strong>: Gabriel Pivetti" +
                        "<br><strong>Email</strong>: gapivetti@gmail.com" +
                        "<br><strong>Mobile</strong>: (18) 991199597")
                .version("v1.0.0");
    }
}
