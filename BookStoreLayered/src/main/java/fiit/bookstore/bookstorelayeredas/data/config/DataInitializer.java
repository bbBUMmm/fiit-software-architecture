package fiit.bookstore.bookstorelayeredas.data.config;

import fiit.bookstore.bookstorelayeredas.data.entity.Book;
import fiit.bookstore.bookstorelayeredas.data.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Configuration class for initializing sample data.
 * Demonstrates:
 * - Configuration class pattern
 * - Factory pattern via @Bean methods
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {
        return args -> {
            logger.info("Initializing database with sample books...");

            // Create sample books using Builder pattern
            Book book1 = Book.builder()
                    .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                    .author("Robert C. Martin")
                    .isbn("9780132350884")
                    .price(new BigDecimal("39.99"))
                    .publisher("Prentice Hall")
                    .publicationYear(2008)
                    .genre("Programming")
                    .description("A handbook of agile software craftsmanship")
                    .quantity(25)
                    .build();

            Book book2 = Book.builder()
                    .title("Design Patterns: Elements of Reusable Object-Oriented Software")
                    .author("Gang of Four")
                    .isbn("9780201633610")
                    .price(new BigDecimal("54.99"))
                    .publisher("Addison-Wesley")
                    .publicationYear(1994)
                    .genre("Programming")
                    .description("The classic book on design patterns")
                    .quantity(15)
                    .build();

            Book book3 = Book.builder()
                    .title("Domain-Driven Design: Tackling Complexity in the Heart of Software")
                    .author("Eric Evans")
                    .isbn("9780321125217")
                    .price(new BigDecimal("62.99"))
                    .publisher("Addison-Wesley")
                    .publicationYear(2003)
                    .genre("Software Architecture")
                    .description("The definitive guide to domain-driven design")
                    .quantity(10)
                    .build();

            Book book4 = Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .isbn("9780134685991")
                    .price(new BigDecimal("45.00"))
                    .publisher("Addison-Wesley")
                    .publicationYear(2017)
                    .genre("Programming")
                    .description("Best practices for the Java platform")
                    .quantity(30)
                    .build();

            Book book5 = Book.builder()
                    .title("The Pragmatic Programmer")
                    .author("David Thomas, Andrew Hunt")
                    .isbn("9780135957059")
                    .price(new BigDecimal("49.99"))
                    .publisher("Addison-Wesley")
                    .publicationYear(2019)
                    .genre("Programming")
                    .description("Your journey to mastery")
                    .quantity(20)
                    .build();

            Book book6 = Book.builder()
                    .title("Refactoring: Improving the Design of Existing Code")
                    .author("Martin Fowler")
                    .isbn("9780134757599")
                    .price(new BigDecimal("47.99"))
                    .publisher("Addison-Wesley")
                    .publicationYear(2018)
                    .genre("Programming")
                    .description("The definitive guide to refactoring")
                    .quantity(18)
                    .build();

            Book book7 = Book.builder()
                    .title("Head First Design Patterns")
                    .author("Eric Freeman, Elisabeth Robson")
                    .isbn("9780596007126")
                    .price(new BigDecimal("44.99"))
                    .publisher("O'Reilly Media")
                    .publicationYear(2004)
                    .genre("Programming")
                    .description("A brain-friendly guide to design patterns")
                    .quantity(22)
                    .build();

            Book book8 = Book.builder()
                    .title("1984")
                    .author("George Orwell")
                    .isbn("9780451524935")
                    .price(new BigDecimal("9.99"))
                    .publisher("Signet Classic")
                    .publicationYear(1949)
                    .genre("Fiction")
                    .description("A dystopian social science fiction novel")
                    .quantity(50)
                    .build();

            Book book9 = Book.builder()
                    .title("To Kill a Mockingbird")
                    .author("Harper Lee")
                    .isbn("9780061120084")
                    .price(new BigDecimal("14.99"))
                    .publisher("Harper Perennial")
                    .publicationYear(1960)
                    .genre("Fiction")
                    .description("A classic of modern American literature")
                    .quantity(35)
                    .build();

            Book book10 = Book.builder()
                    .title("Building Microservices")
                    .author("Sam Newman")
                    .isbn("9781492034025")
                    .price(new BigDecimal("52.99"))
                    .publisher("O'Reilly Media")
                    .publicationYear(2021)
                    .genre("Software Architecture")
                    .description("Designing fine-grained systems")
                    .quantity(12)
                    .build();

            // Save all books
            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            bookRepository.save(book4);
            bookRepository.save(book5);
            bookRepository.save(book6);
            bookRepository.save(book7);
            bookRepository.save(book8);
            bookRepository.save(book9);
            bookRepository.save(book10);

            logger.info("Database initialized with {} sample books", bookRepository.count());
        };
    }
}

