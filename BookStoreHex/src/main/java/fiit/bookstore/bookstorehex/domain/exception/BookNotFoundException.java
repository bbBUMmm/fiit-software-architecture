package fiit.bookstore.bookstorehex.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested book is not found.
 */
public class BookNotFoundException extends DomainException {

    private static final String ERROR_CODE = "BOOK_NOT_FOUND";

    public BookNotFoundException(UUID id) {
        super("Book not found with id: " + id, ERROR_CODE);
    }

    public BookNotFoundException(String isbn) {
        super("Book not found with ISBN: " + isbn, ERROR_CODE);
    }
}

