package fiit.bookstore.bookstorelayeredas.business.service;

import fiit.bookstore.bookstorelayeredas.business.dto.BookResponse;
import fiit.bookstore.bookstorelayeredas.business.dto.CreateBookRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.UpdateBookRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for Book operations.
 * Demonstrates:
 * - Abstraction: Interface defines contract without implementation details
 * - Polymorphism: Allows different implementations (e.g., mock for testing)
 * - Separation of concerns: Business logic separated from data access
 */
public interface BookService {

    /**
     * Create a new book
     * @param request the book creation request
     * @return the created book response
     */
    BookResponse createBook(CreateBookRequest request);

    /**
     * Get a book by its ID
     * @param id the book ID
     * @return the book response
     */
    BookResponse getBookById(UUID id);

    /**
     * Get a book by its ISBN
     * @param isbn the book ISBN
     * @return the book response
     */
    BookResponse getBookByIsbn(String isbn);

    /**
     * Get all books
     * @return list of all books
     */
    List<BookResponse> getAllBooks();

    /**
     * Update an existing book
     * @param id the book ID
     * @param request the update request
     * @return the updated book response
     */
    BookResponse updateBook(UUID id, UpdateBookRequest request);

    /**
     * Delete a book by its ID
     * @param id the book ID
     */
    void deleteBook(UUID id);

    /**
     * Search books by author
     * @param author the author name
     * @return list of matching books
     */
    List<BookResponse> findBooksByAuthor(String author);

    /**
     * Search books by title
     * @param title the title to search
     * @return list of matching books
     */
    List<BookResponse> findBooksByTitle(String title);

    /**
     * Search books by genre
     * @param genre the genre to search
     * @return list of matching books
     */
    List<BookResponse> findBooksByGenre(String genre);

    /**
     * Get all available books (with stock)
     * @return list of available books
     */
    List<BookResponse> getAvailableBooks();

    /**
     * Search books by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within price range
     */
    List<BookResponse> findBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Search books by a general search term
     * @param searchTerm the term to search in title, author, or genre
     * @return list of matching books
     */
    List<BookResponse> searchBooks(String searchTerm);

    /**
     * Update book stock quantity
     * @param id the book ID
     * @param quantity the new quantity
     * @return the updated book response
     */
    BookResponse updateStock(UUID id, int quantity);
}

