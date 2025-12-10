package fiit.bookstore.bookstorehex.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when a purchase is not found.
 */
public class PurchaseNotFoundException extends DomainException {

    private static final String ERROR_CODE = "PURCHASE_NOT_FOUND";

    public PurchaseNotFoundException(String orderNumber) {
        super(String.format("Purchase with order number '%s' not found", orderNumber), ERROR_CODE);
    }

    public PurchaseNotFoundException(UUID id) {
        super(String.format("Purchase with ID '%s' not found", id), ERROR_CODE);
    }
}

