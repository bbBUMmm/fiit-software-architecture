package fiit.bookstore.bookstorehex.apispec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for a single item in a purchase request.
 */
public class PurchaseItemRequest {

    @NotNull(message = "Book ID is required")
    private UUID bookId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public PurchaseItemRequest() {}

    public PurchaseItemRequest(UUID bookId, Integer quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public UUID getBookId() { return bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
