package fiit.bookstore.bookstorehex.domain.service;

import fiit.bookstore.bookstorehex.domain.entity.Book;
import fiit.bookstore.bookstorehex.domain.exception.BookNotFoundException;
import fiit.bookstore.bookstorehex.domain.exception.DuplicateIsbnException;
import fiit.bookstore.bookstorehex.domain.port.inbound.BookUseCase;
import fiit.bookstore.bookstorehex.domain.port.outbound.BookRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Domain service implementing BookUseCase.
 * Contains the core business logic for book operations.
 *
 * In hexagonal architecture, domain services are pure and framework-agnostic.
 * They only depend on domain entities and ports.
 */
public class BookService implements BookUseCase {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) {
        // Business validation: Check for duplicate ISBN
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }

        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(UUID id, Book bookUpdate) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Update only non-null fields (partial update pattern)
        if (bookUpdate.getTitle() != null) {
            existingBook.setTitle(bookUpdate.getTitle());
        }
        if (bookUpdate.getAuthor() != null) {
            existingBook.setAuthor(bookUpdate.getAuthor());
        }
        if (bookUpdate.getPrice() != null) {
            existingBook.setPrice(bookUpdate.getPrice());
        }
        if (bookUpdate.getPublisher() != null) {
            existingBook.setPublisher(bookUpdate.getPublisher());
        }
        if (bookUpdate.getPublicationYear() != null) {
            existingBook.setPublicationYear(bookUpdate.getPublicationYear());
        }
        if (bookUpdate.getGenre() != null) {
            existingBook.setGenre(bookUpdate.getGenre());
        }
        if (bookUpdate.getDescription() != null) {
            existingBook.setDescription(bookUpdate.getDescription());
        }
        if (bookUpdate.getQuantity() != null) {
            existingBook.setQuantity(bookUpdate.getQuantity());
        }

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findAllAvailable();
    }

    @Override
    public List<Book> findBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return bookRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Book> searchBooks(String searchTerm) {
        return bookRepository.searchBooks(searchTerm);
    }

    @Override
    public Book updateStock(UUID id, int quantity) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        book.setQuantity(quantity);
        return bookRepository.save(book);
    }
}
