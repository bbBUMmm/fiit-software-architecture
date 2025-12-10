package fiit.bookstore.bookstorelayeredas.data.entity;

import java.math.BigDecimal;

/**
 * PurchaseItem entity representing a single item in a purchase.
 */
public class PurchaseItem extends BaseEntity {

    private Purchase purchase;
    private Book book;
    
    // Snapshot of book details at time of purchase (in case book is updated/deleted later)
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Default constructor for JPA
    public PurchaseItem() {
        super();
    }

    /**
     * Create a purchase item from a book.
     * Captures the book details at the time of purchase.
     */
    public PurchaseItem(Book book, int quantity) {
        super();
        this.book = book;
        this.bookTitle = book.getTitle();
        this.bookAuthor = book.getAuthor();
        this.bookIsbn = book.getIsbn();
        this.quantity = quantity;
        this.unitPrice = book.getPrice();
        calculateSubtotal();
    }

    /**
     * Calculate the subtotal for this item.
     * Domain logic encapsulated in entity.
     */
    public void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    // Getters and Setters
    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
        calculateSubtotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
