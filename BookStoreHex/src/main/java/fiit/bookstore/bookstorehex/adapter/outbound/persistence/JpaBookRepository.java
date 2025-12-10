package fiit.bookstore.bookstorehex.adapter.outbound.persistence;

import fiit.bookstore.bookstorehex.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Book persistence.
 * This is internal to the outbound adapter and should not be exposed to the domain.
 */
@Repository
interface JpaBookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByGenreIgnoreCase(String genre);

    @Query("SELECT b FROM Book b WHERE b.quantity > 0")
    List<Book> findAllAvailable();

    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);

    boolean existsByIsbn(String isbn);
}

