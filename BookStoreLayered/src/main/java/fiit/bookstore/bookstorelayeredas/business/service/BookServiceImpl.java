package fiit.bookstore.bookstorelayeredas.business.service;

import fiit.bookstore.bookstorelayeredas.business.dto.BookResponse;
import fiit.bookstore.bookstorelayeredas.business.dto.CreateBookRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.UpdateBookRequest;
import fiit.bookstore.bookstorelayeredas.business.exception.BookNotFoundException;
import fiit.bookstore.bookstorelayeredas.business.exception.DuplicateIsbnException;
import fiit.bookstore.bookstorelayeredas.data.entity.Book;
import fiit.bookstore.bookstorelayeredas.data.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of BookService.
 * Demonstrates:
 * - Polymorphism: Implements BookService interface
 * - Encapsulation: Business logic encapsulated in service methods
 * - Single Responsibility: Handles only book-related business logic
 * - Dependency Injection: Repository injected via constructor
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    // Constructor injection - promotes loose coupling
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        logger.info("Creating new book with ISBN: {}", request.getIsbn());

        // Business validation: Check for duplicate ISBN
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException(request.getIsbn());
        }

        // Map DTO to entity
        Book book = mapToEntity(request);

        // Persist the entity
        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully with ID: {}", savedBook.getId());

        // Map entity to response DTO
        return BookResponse.fromEntity(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(UUID id) {
        logger.debug("Fetching book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return BookResponse.fromEntity(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        logger.debug("Fetching book with ISBN: {}", isbn);

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));

        return BookResponse.fromEntity(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        logger.debug("Fetching all books");

        return bookRepository.findAll().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse updateBook(UUID id, UpdateBookRequest request) {
        logger.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Update only non-null fields (partial update pattern)
        updateEntityFromRequest(existingBook, request);

        Book updatedBook = bookRepository.save(existingBook);
        logger.info("Book updated successfully with ID: {}", id);

        return BookResponse.fromEntity(updatedBook);
    }

    @Override
    public void deleteBook(UUID id) {
        logger.info("Deleting book with ID: {}", id);

        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        bookRepository.deleteById(id);
        logger.info("Book deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByAuthor(String author) {
        logger.debug("Searching books by author: {}", author);

        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByTitle(String title) {
        logger.debug("Searching books by title: {}", title);

        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByGenre(String genre) {
        logger.debug("Searching books by genre: {}", genre);

        return bookRepository.findByGenreIgnoreCase(genre).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAvailableBooks() {
        logger.debug("Fetching available books");

        return bookRepository.findAllAvailable().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Searching books by price range: {} - {}", minPrice, maxPrice);

        return bookRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String searchTerm) {
        logger.debug("Searching books with term: {}", searchTerm);

        return bookRepository.searchBooks(searchTerm).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse updateStock(UUID id, int quantity) {
        logger.info("Updating stock for book ID: {} to quantity: {}", id, quantity);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        book.setQuantity(quantity);
        Book updatedBook = bookRepository.save(book);

        logger.info("Stock updated successfully for book ID: {}", id);
        return BookResponse.fromEntity(updatedBook);
    }

    /**
     * Maps CreateBookRequest DTO to Book entity.
     * Encapsulates the mapping logic.
     */
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

    /**
     * Updates entity fields from UpdateBookRequest DTO.
     * Only updates non-null fields (partial update pattern).
     */
    private void updateEntityFromRequest(Book book, UpdateBookRequest request) {
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublisher() != null) {
            book.setPublisher(request.getPublisher());
        }
        if (request.getPublicationYear() != null) {
            book.setPublicationYear(request.getPublicationYear());
        }
        if (request.getGenre() != null) {
            book.setGenre(request.getGenre());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getQuantity() != null) {
            book.setQuantity(request.getQuantity());
        }
    }
}

