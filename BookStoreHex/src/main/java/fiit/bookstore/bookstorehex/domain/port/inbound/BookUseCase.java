package fiit.bookstore.bookstorehex.domain.port.inbound;

import fiit.bookstore.bookstorehex.domain.entity.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port (use case) for Book operations.
 * In hexagonal architecture, inbound ports define the operations that the application
 * exposes to the outside world (e.g., REST controllers, CLI, etc.).
 *
 * This interface is part of the domain layer and is framework-agnostic.
 */
public interface BookUseCase {

    /**
     * Create a new book
     * @param book the book to create
     * @return the created book
     */
    Book createBook(Book book);

    /**
     * Get a book by its ID
     * @param id the book ID
     * @return the book
     */
    Book getBookById(UUID id);

    /**
     * Get a book by its ISBN
     * @param isbn the book ISBN
     * @return the book
     */
    Book getBookByIsbn(String isbn);

    /**
     * Get all books
     * @return list of all books
     */
    List<Book> getAllBooks();

    /**
     * Update an existing book
     * @param id the book ID
     * @param book the updated book data
     * @return the updated book
     */
    Book updateBook(UUID id, Book book);

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
    List<Book> findBooksByAuthor(String author);

    /**
     * Search books by title
     * @param title the title to search
     * @return list of matching books
     */
    List<Book> findBooksByTitle(String title);

    /**
     * Search books by genre
     * @param genre the genre to search
     * @return list of matching books
     */
    List<Book> findBooksByGenre(String genre);

    /**
     * Get all available books (with stock)
     * @return list of available books
     */
    List<Book> getAvailableBooks();

    /**
     * Search books by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within price range
     */
    List<Book> findBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Search books by a general search term
     * @param searchTerm the term to search in title, author, or genre
     * @return list of matching books
     */
    List<Book> searchBooks(String searchTerm);

    /**
     * Update the stock quantity of a book
     * @param id the book ID
     * @param quantity the new quantity
     * @return the updated book
     */
    Book updateStock(UUID id, int quantity);
}

