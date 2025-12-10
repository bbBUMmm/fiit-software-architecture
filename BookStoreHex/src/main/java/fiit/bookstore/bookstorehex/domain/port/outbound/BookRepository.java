package fiit.bookstore.bookstorehex.domain.port.outbound;

import fiit.bookstore.bookstorehex.domain.entity.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port (repository interface) for Book persistence.
 * In hexagonal architecture, outbound ports define the operations that the domain
 * needs from the outside world (e.g., database, external services, etc.).
 *
 * This interface is part of the domain layer and is framework-agnostic.
 * The actual implementation (adapter) will be in the infrastructure layer.
 */
public interface BookRepository {

    /**
     * Save a book (create or update)
     * @param book the book to save
     * @return the saved book
     */
    Book save(Book book);

    /**
     * Find a book by its ID
     * @param id the book ID
     * @return Optional containing the book if found
     */
    Optional<Book> findById(UUID id);

    /**
     * Find a book by its ISBN
     * @param isbn the ISBN to search for
     * @return Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find all books
     * @return list of all books
     */
    List<Book> findAll();

    /**
     * Find all books by author (partial match, case-insensitive)
     * @param author the author name
     * @return list of books by the specified author
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Find all books by title (partial match, case-insensitive)
     * @param title the title to search for
     * @return list of books matching the title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find all books by genre (case-insensitive)
     * @param genre the genre to search for
     * @return list of books in the specified genre
     */
    List<Book> findByGenreIgnoreCase(String genre);

    /**
     * Find all available books (quantity > 0)
     * @return list of available books
     */
    List<Book> findAllAvailable();

    /**
     * Find books by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    List<Book> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Search books by multiple criteria (title, author, or genre)
     * @param searchTerm term to search
     * @return list of matching books
     */
    List<Book> searchBooks(String searchTerm);

    /**
     * Check if a book with the given ISBN already exists
     * @param isbn the ISBN to check
     * @return true if exists, false otherwise
     */
    boolean existsByIsbn(String isbn);

    /**
     * Check if a book with the given ID exists
     * @param id the ID to check
     * @return true if exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Delete a book by its ID
     * @param id the book ID
     */
    void deleteById(UUID id);
}

