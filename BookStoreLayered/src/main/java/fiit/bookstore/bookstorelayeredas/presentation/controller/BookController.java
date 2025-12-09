package fiit.bookstore.bookstorelayeredas.presentation.controller;

import fiit.bookstore.bookstorelayeredas.business.dto.BookResponse;
import fiit.bookstore.bookstorelayeredas.business.dto.CreateBookRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.UpdateBookRequest;
import fiit.bookstore.bookstorelayeredas.business.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Book operations.
 * Demonstrates:
 * - Presentation Layer: Handles HTTP requests and responses
 * - Separation of Concerns: Only handles HTTP-specific logic
 * - Dependency Injection: Service injected via constructor
 * - Single Responsibility: Only responsible for HTTP handling
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    // Constructor injection - promotes testability and loose coupling
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Create a new book
     * POST /api/v1/books
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        logger.info("Received request to create book with ISBN: {}", request.getIsbn());
        BookResponse response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a book by ID
     * GET /api/v1/books/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        logger.info("Received request to get book with ID: {}", id);
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a book by ISBN
     * GET /api/v1/books/isbn/{isbn}
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        logger.info("Received request to get book with ISBN: {}", isbn);
        BookResponse response = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all books
     * GET /api/v1/books
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        logger.info("Received request to get all books");
        List<BookResponse> response = bookService.getAllBooks();
        return ResponseEntity.ok(response);
    }

    /**
     * Update a book
     * PUT /api/v1/books/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookRequest request) {
        logger.info("Received request to update book with ID: {}", id);
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a book
     * DELETE /api/v1/books/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        logger.info("Received request to delete book with ID: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search books by author
     * GET /api/v1/books/search/author?name={author}
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> findBooksByAuthor(@RequestParam String name) {
        logger.info("Received request to search books by author: {}", name);
        List<BookResponse> response = bookService.findBooksByAuthor(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Search books by title
     * GET /api/v1/books/search/title?name={title}
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponse>> findBooksByTitle(@RequestParam String name) {
        logger.info("Received request to search books by title: {}", name);
        List<BookResponse> response = bookService.findBooksByTitle(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Search books by genre
     * GET /api/v1/books/search/genre?name={genre}
     */
    @GetMapping("/search/genre")
    public ResponseEntity<List<BookResponse>> findBooksByGenre(@RequestParam String name) {
        logger.info("Received request to search books by genre: {}", name);
        List<BookResponse> response = bookService.findBooksByGenre(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Get available books (with stock)
     * GET /api/v1/books/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        logger.info("Received request to get available books");
        List<BookResponse> response = bookService.getAvailableBooks();
        return ResponseEntity.ok(response);
    }

    /**
     * Search books by price range
     * GET /api/v1/books/search/price?min={minPrice}&max={maxPrice}
     */
    @GetMapping("/search/price")
    public ResponseEntity<List<BookResponse>> findBooksByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        logger.info("Received request to search books by price range: {} - {}", min, max);
        List<BookResponse> response = bookService.findBooksByPriceRange(min, max);
        return ResponseEntity.ok(response);
    }

    /**
     * General search (searches in title, author, genre)
     * GET /api/v1/books/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String term) {
        logger.info("Received request to search books with term: {}", term);
        List<BookResponse> response = bookService.searchBooks(term);
        return ResponseEntity.ok(response);
    }

    /**
     * Update book stock
     * PATCH /api/v1/books/{id}/stock?quantity={quantity}
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<BookResponse> updateStock(
            @PathVariable UUID id,
            @RequestParam int quantity) {
        logger.info("Received request to update stock for book ID: {} to {}", id, quantity);
        BookResponse response = bookService.updateStock(id, quantity);
        return ResponseEntity.ok(response);
    }
}

