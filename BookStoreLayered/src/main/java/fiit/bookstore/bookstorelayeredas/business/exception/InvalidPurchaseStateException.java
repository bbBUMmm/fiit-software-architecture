package fiit.bookstore.bookstorelayeredas.business.exception;

/**
 * Exception thrown when trying to perform an invalid operation on a purchase.
 */
public class InvalidPurchaseStateException extends BusinessException {

    private static final String ERROR_CODE = "INVALID_PURCHASE_STATE";

    public InvalidPurchaseStateException(String message) {
        super(message, ERROR_CODE);
    }
}
