package fiit.bookstore.bookstorelayeredas.business.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested book is not found.
 * Demonstrates inheritance from BusinessException.
 */
public class BookNotFoundException extends BusinessException {

    private static final String ERROR_CODE = "BOOK_NOT_FOUND";

    public BookNotFoundException(UUID id) {
        super("Book not found with id: " + id, ERROR_CODE);
    }

    public BookNotFoundException(String isbn) {
        super("Book not found with ISBN: " + isbn, ERROR_CODE);
    }
}

