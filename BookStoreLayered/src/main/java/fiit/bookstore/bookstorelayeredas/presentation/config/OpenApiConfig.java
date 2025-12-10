package fiit.bookstore.bookstorelayeredas.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookStoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookStore Layered Architecture API")
                        .description("REST API for BookStore application using Layered Architecture")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BookStore Team")
                                .email("bookstore@fiit.sk")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")));
    }
}

