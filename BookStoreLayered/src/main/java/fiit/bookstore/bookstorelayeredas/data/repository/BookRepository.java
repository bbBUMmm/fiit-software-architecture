package fiit.bookstore.bookstorelayeredas.data.repository;

import fiit.bookstore.bookstorelayeredas.data.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Book entity.
 * Demonstrates:
 * - Abstraction: Interface defines contract for data access
 * - Polymorphism: Spring Data JPA provides implementation at runtime
 * - Separation of concerns: Data access logic separated from business logic
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    /**
     * Find a book by its ISBN
     * @param isbn the ISBN to search for
     * @return Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find all books by author
     * @param author the author name
     * @return list of books by the specified author
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Find all books by title (partial match)
     * @param title the title to search for
     * @return list of books matching the title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find all books by genre
     * @param genre the genre to search for
     * @return list of books in the specified genre
     */
    List<Book> findByGenreIgnoreCase(String genre);

    /**
     * Find all books by publisher
     * @param publisher the publisher name
     * @return list of books from the specified publisher
     */
    List<Book> findByPublisherContainingIgnoreCase(String publisher);

    /**
     * Find all available books (quantity > 0)
     * @return list of available books
     */
    @Query("SELECT b FROM Book b WHERE b.quantity > 0")
    List<Book> findAllAvailable();

    /**
     * Find books by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find books by publication year range
     * @param startYear start year
     * @param endYear end year
     * @return list of books published within the year range
     */
    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);

    /**
     * Check if a book with the given ISBN already exists
     * @param isbn the ISBN to check
     * @return true if exists, false otherwise
     */
    boolean existsByIsbn(String isbn);

    /**
     * Search books by multiple criteria
     * @param searchTerm term to search in title, author, or genre
     * @return list of matching books
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
}

