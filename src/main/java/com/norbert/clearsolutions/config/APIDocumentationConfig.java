package com.norbert.clearsolutions.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ClearSolutions User API REST",
                version = "1.0",
                description = "API REST for User Operations",
                contact = @Contact(name = "Norbert Tovt", url = "https://github.com/n0rb33rt/ClearSolutionsTestTask", email = "n0rb3rt.work@gmail.com")
        )
)
public class APIDocumentationConfig {

}