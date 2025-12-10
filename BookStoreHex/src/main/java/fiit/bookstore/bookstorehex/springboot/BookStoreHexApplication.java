package fiit.bookstore.bookstorehex.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class for BookStore Hexagonal Architecture.
 *
 * In hexagonal architecture, this is the composition root where all the
 * adapters and configurations come together.
 */
@SpringBootApplication(scanBasePackages = "fiit.bookstore.bookstorehex")
@EntityScan(basePackages = "fiit.bookstore.bookstorehex.domain.entity")
@EnableJpaRepositories(basePackages = "fiit.bookstore.bookstorehex.adapter.outbound.persistence")
public class BookStoreHexApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreHexApplication.class, args);
    }

}
