package fiit.bookstore.bookstorehex.domain.exception;

/**
 * Exception thrown when an invalid discount code is used.
 */
public class InvalidDiscountCodeException extends DomainException {

    private static final String ERROR_CODE = "INVALID_DISCOUNT_CODE";

    public InvalidDiscountCodeException(String code) {
        super(String.format("Invalid or expired discount code: '%s'", code), ERROR_CODE);
    }
}

