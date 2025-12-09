package fiit.bookstore.bookstorelayeredas.business.exception;

/**
 * Exception thrown when there is insufficient stock for an operation.
 * Demonstrates inheritance from BusinessException.
 */
public class InsufficientStockException extends BusinessException {

    private static final String ERROR_CODE = "INSUFFICIENT_STOCK";

    public InsufficientStockException(String isbn, int requested, int available) {
        super(String.format("Insufficient stock for book with ISBN '%s'. Requested: %d, Available: %d",
                isbn, requested, available), ERROR_CODE);
    }
}

