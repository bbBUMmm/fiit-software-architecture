package fiit.bookstore.bookstorehex.adapter.inbound.rest;

import fiit.bookstore.bookstorehex.apispec.dto.BookResponse;
import fiit.bookstore.bookstorehex.apispec.dto.CreateBookRequest;
import fiit.bookstore.bookstorehex.apispec.dto.UpdateBookRequest;
import fiit.bookstore.bookstorehex.domain.entity.Book;
import fiit.bookstore.bookstorehex.domain.port.inbound.BookUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Book operations.
 * This is an inbound adapter in hexagonal architecture.
 * It adapts HTTP requests to the domain's BookUseCase port.
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookUseCase bookUseCase;

    public BookController(BookUseCase bookUseCase) {
        this.bookUseCase = bookUseCase;
    }

    /**
     * Create a new book
     * POST /api/v1/books
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        logger.info("Received request to create book with ISBN: {}", request.getIsbn());

        Book book = mapToEntity(request);
        Book createdBook = bookUseCase.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(BookResponse.fromEntity(createdBook));
    }

    /**
     * Get a book by ID
     * GET /api/v1/books/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        logger.info("Received request to get book with ID: {}", id);
        Book book = bookUseCase.getBookById(id);
        return ResponseEntity.ok(BookResponse.fromEntity(book));
    }

    /**
     * Get a book by ISBN
     * GET /api/v1/books/isbn/{isbn}
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        logger.info("Received request to get book with ISBN: {}", isbn);
        Book book = bookUseCase.getBookByIsbn(isbn);
        return ResponseEntity.ok(BookResponse.fromEntity(book));
    }

    /**
     * Get all books
     * GET /api/v1/books
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        logger.info("Received request to get all books");
        List<BookResponse> response = bookUseCase.getAllBooks().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
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

        Book bookUpdate = mapToEntity(request);
        Book updatedBook = bookUseCase.updateBook(id, bookUpdate);

        return ResponseEntity.ok(BookResponse.fromEntity(updatedBook));
    }

    /**
     * Delete a book
     * DELETE /api/v1/books/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        logger.info("Received request to delete book with ID: {}", id);
        bookUseCase.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search books by author
     * GET /api/v1/books/search/author?name={author}
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> findBooksByAuthor(@RequestParam String name) {
        logger.info("Received request to search books by author: {}", name);
        List<BookResponse> response = bookUseCase.findBooksByAuthor(name).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Search books by title
     * GET /api/v1/books/search/title?name={title}
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponse>> findBooksByTitle(@RequestParam String name) {
        logger.info("Received request to search books by title: {}", name);
        List<BookResponse> response = bookUseCase.findBooksByTitle(name).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Search books by genre
     * GET /api/v1/books/search/genre?name={genre}
     */
    @GetMapping("/search/genre")
    public ResponseEntity<List<BookResponse>> findBooksByGenre(@RequestParam String name) {
        logger.info("Received request to search books by genre: {}", name);
        List<BookResponse> response = bookUseCase.findBooksByGenre(name).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get available books (with stock)
     * GET /api/v1/books/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        logger.info("Received request to get available books");
        List<BookResponse> response = bookUseCase.getAvailableBooks().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
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
        List<BookResponse> response = bookUseCase.findBooksByPriceRange(min, max).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * General search (searches in title, author, genre)
     * GET /api/v1/books/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String term) {
        logger.info("Received request to search books with term: {}", term);
        List<BookResponse> response = bookUseCase.searchBooks(term).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
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
        Book book = bookUseCase.updateStock(id, quantity);
        return ResponseEntity.ok(BookResponse.fromEntity(book));
    }

    // ==================== Mapping Methods ====================

    private Book mapToEntity(CreateBookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publisher(request.getPublisher())
                .publicationYear(request.getPublicationYear())
                .genre(request.getGenre())
                .description(request.getDescription())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .build();
    }

    private Book mapToEntity(UpdateBookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setGenre(request.getGenre());
        book.setDescription(request.getDescription());
        book.setQuantity(request.getQuantity());
        return book;
    }
}

