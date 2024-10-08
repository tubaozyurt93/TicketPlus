package com.patika.ticketpluspaymentservice.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class OpenapiConfig {

    @Bean
    public OpenAPI teknikservisOpenAPI () {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Plus Payment Servisi")
                        .description("It processes payment services.")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Ticket Plus Project Documentation")
                        .url("https://springdoc.org/"));
    }
}
