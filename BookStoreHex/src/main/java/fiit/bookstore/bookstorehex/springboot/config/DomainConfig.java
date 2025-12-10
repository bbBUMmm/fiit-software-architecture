package fiit.bookstore.bookstorehex.springboot.config;

import fiit.bookstore.bookstorehex.domain.port.inbound.BookUseCase;
import fiit.bookstore.bookstorehex.domain.port.inbound.PurchaseUseCase;
import fiit.bookstore.bookstorehex.domain.port.outbound.BookRepository;
import fiit.bookstore.bookstorehex.domain.port.outbound.PurchaseRepository;
import fiit.bookstore.bookstorehex.domain.service.BookService;
import fiit.bookstore.bookstorehex.domain.service.PurchaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the domain layer.
 * This configuration wires the domain services with their required ports.
 *
 * In hexagonal architecture, this is where the dependency injection happens,
 * connecting inbound ports (use cases) with their implementations (domain services)
 * and injecting outbound ports (repositories) into the services.
 */
@Configuration
public class DomainConfig {

    /**
     * Creates the BookUseCase (inbound port) bean.
     * The BookService is the domain implementation that uses the BookRepository (outbound port).
     */
    @Bean
    public BookUseCase bookUseCase(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    /**
     * Creates the PurchaseUseCase (inbound port) bean.
     * The PurchaseService is the domain implementation that uses both repositories.
     */
    @Bean
    public PurchaseUseCase purchaseUseCase(PurchaseRepository purchaseRepository, BookRepository bookRepository) {
        return new PurchaseService(purchaseRepository, bookRepository);
    }
}

