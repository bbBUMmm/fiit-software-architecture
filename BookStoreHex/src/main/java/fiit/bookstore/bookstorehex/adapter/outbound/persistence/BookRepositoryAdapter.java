package fiit.bookstore.bookstorehex.adapter.outbound.persistence;

import fiit.bookstore.bookstorehex.domain.entity.Book;
import fiit.bookstore.bookstorehex.domain.port.outbound.BookRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementation for BookRepository outbound port.
 * This adapter translates between the domain port and the JPA repository.
 *
 * In hexagonal architecture, this is the outbound adapter that connects
 * the domain to the database infrastructure.
 */
@Component
public class BookRepositoryAdapter implements BookRepository {

    private final JpaBookRepository jpaBookRepository;

    public BookRepositoryAdapter(JpaBookRepository jpaBookRepository) {
        this.jpaBookRepository = jpaBookRepository;
    }

    @Override
    public Book save(Book book) {
        return jpaBookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return jpaBookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jpaBookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> findAll() {
        return jpaBookRepository.findAll();
    }

    @Override
    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        return jpaBookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return jpaBookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> findByGenreIgnoreCase(String genre) {
        return jpaBookRepository.findByGenreIgnoreCase(genre);
    }

    @Override
    public List<Book> findAllAvailable() {
        return jpaBookRepository.findAllAvailable();
    }

    @Override
    public List<Book> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return jpaBookRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Book> searchBooks(String searchTerm) {
        return jpaBookRepository.searchBooks(searchTerm);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return jpaBookRepository.existsByIsbn(isbn);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaBookRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaBookRepository.deleteById(id);
    }
}

