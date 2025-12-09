package fiit.bookstore.bookstorelayeredas.business.exception;

/**
 * Exception thrown when a duplicate ISBN is detected.
 * Demonstrates inheritance from BusinessException.
 */
public class DuplicateIsbnException extends BusinessException {

    private static final String ERROR_CODE = "DUPLICATE_ISBN";

    public DuplicateIsbnException(String isbn) {
        super("Book with ISBN '" + isbn + "' already exists", ERROR_CODE);
    }
}

