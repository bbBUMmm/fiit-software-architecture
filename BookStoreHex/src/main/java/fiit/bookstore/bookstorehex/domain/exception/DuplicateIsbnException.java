package fiit.bookstore.bookstorehex.domain.exception;

/**
 * Exception thrown when a duplicate ISBN is detected.
 */
public class DuplicateIsbnException extends DomainException {

    private static final String ERROR_CODE = "DUPLICATE_ISBN";

    public DuplicateIsbnException(String isbn) {
        super("Book with ISBN '" + isbn + "' already exists", ERROR_CODE);
    }
}

