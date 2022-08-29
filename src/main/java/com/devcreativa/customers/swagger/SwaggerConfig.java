package com.devcreativa.customers.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Customer manager - RestFull Service")
                .description("RestFull Service: Crud project of customer manager.")
                .version("v0.0.1")
                .license(new License()
                    .name("Apache 2.0").url("http://springdoc.org"))
                .description("Customer manager Wiki Documentation")
                .contact(new Contact()
                    .email("wveraguerra@gmail.com").url("https://www.linkedin.com/in/wualtervera")))
            ;
    }
}
