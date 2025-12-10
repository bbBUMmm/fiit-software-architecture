package fiit.bookstore.bookstorelayeredas.business.dto;

import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseItem;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for purchase item response.
 * Represents details of a purchased book item.
 */
public class PurchaseItemResponse {

    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Default constructor
    public PurchaseItemResponse() {
    }

    /**
     * Factory method to create PurchaseItemResponse from PurchaseItem entity.
     */
    public static PurchaseItemResponse fromEntity(PurchaseItem item) {
        PurchaseItemResponse response = new PurchaseItemResponse();
        response.setId(item.getId());
        response.setBookId(item.getBook().getId());
        response.setBookTitle(item.getBookTitle());
        response.setBookAuthor(item.getBookAuthor());
        response.setBookIsbn(item.getBookIsbn());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
