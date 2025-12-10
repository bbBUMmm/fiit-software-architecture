package fiit.bookstore.bookstorehex.domain.exception;

/**
 * Exception thrown when there is insufficient stock for an operation.
 */
public class InsufficientStockException extends DomainException {

    private static final String ERROR_CODE = "INSUFFICIENT_STOCK";

    public InsufficientStockException(String isbn, int requested, int available) {
        super(String.format("Insufficient stock for book with ISBN '%s'. Requested: %d, Available: %d",
                isbn, requested, available), ERROR_CODE);
    }
}

