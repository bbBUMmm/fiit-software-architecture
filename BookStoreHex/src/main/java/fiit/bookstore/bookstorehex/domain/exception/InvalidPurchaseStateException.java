package fiit.bookstore.bookstorehex.domain.exception;

/**
 * Exception thrown when trying to perform an invalid operation on a purchase.
 */
public class InvalidPurchaseStateException extends DomainException {

    private static final String ERROR_CODE = "INVALID_PURCHASE_STATE";

    public InvalidPurchaseStateException(String message) {
        super(message, ERROR_CODE);
    }
}

