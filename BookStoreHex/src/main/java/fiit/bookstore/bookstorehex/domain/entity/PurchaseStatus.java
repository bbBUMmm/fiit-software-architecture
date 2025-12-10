package fiit.bookstore.bookstorehex.domain.entity;

/**
 * Enum representing the status of a purchase order.
 */
public enum PurchaseStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String displayName;

    PurchaseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
