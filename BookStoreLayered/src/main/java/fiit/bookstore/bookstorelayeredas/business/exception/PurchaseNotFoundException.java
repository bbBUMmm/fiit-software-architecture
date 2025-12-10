package fiit.bookstore.bookstorelayeredas.business.exception;

/**
 * Exception thrown when a purchase is not found.
 */
public class PurchaseNotFoundException extends BusinessException {

    private static final String ERROR_CODE = "PURCHASE_NOT_FOUND";

    public PurchaseNotFoundException(String orderNumber) {
        super(String.format("Purchase with order number '%s' not found", orderNumber), ERROR_CODE);
    }

    public PurchaseNotFoundException(java.util.UUID id) {
        super(String.format("Purchase with ID '%s' not found", id), ERROR_CODE);
    }
}
