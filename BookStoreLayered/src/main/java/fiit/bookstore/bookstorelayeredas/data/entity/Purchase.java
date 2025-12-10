package fiit.bookstore.bookstorelayeredas.data.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Purchase entity representing a book purchase transaction.
 */
public class Purchase extends BaseEntity {

    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    
    private List<PurchaseItem> items = new ArrayList<>();
    
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private String discountCode;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Integer totalItems = 0;
    
    private PurchaseStatus status = PurchaseStatus.PENDING;
    private LocalDateTime purchaseDate;

    // Default constructor for JPA
    public Purchase() {
        super();
        this.orderNumber = generateOrderNumber();
        this.purchaseDate = LocalDateTime.now();
    }

    /**
     * Generate a unique order number.
     * Format: ORD-{timestamp}-{random}
     */
    private String generateOrderNumber() {
        long timestamp = System.currentTimeMillis();
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format("ORD-%d-%s", timestamp, random);
    }

    /**
     * Add an item to the purchase.
     * Updates totals automatically.
     */
    public void addItem(PurchaseItem item) {
        item.setPurchase(this);
        this.items.add(item);
        recalculateTotals();
    }

    /**
     * Remove an item from the purchase.
     * Updates totals automatically.
     */
    public void removeItem(PurchaseItem item) {
        this.items.remove(item);
        item.setPurchase(null);
        recalculateTotals();
    }

    /**
     * Recalculate all totals based on current items and discount.
     * Core business logic encapsulated in the entity.
     */
    public void recalculateTotals() {
        // Calculate subtotal from all items
        this.subtotal = items.stream()
                .map(PurchaseItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate total items
        this.totalItems = items.stream()
                .mapToInt(PurchaseItem::getQuantity)
                .sum();
        
        // Apply discount
        this.totalAmount = subtotal.subtract(discountAmount);
        
        // Ensure total is not negative
        if (this.totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.totalAmount = BigDecimal.ZERO;
        }
    }

    /**
     * Apply a percentage discount to the order.
     * @param percentage discount percentage (0-100)
     */
    public void applyPercentageDiscount(BigDecimal percentage, String code) {
        if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        this.discountCode = code;
        this.discountAmount = subtotal.multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        recalculateTotals();
    }

    /**
     * Apply a fixed amount discount to the order.
     * @param amount fixed discount amount
     */
    public void applyFixedDiscount(BigDecimal amount, String code) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        this.discountCode = code;
        this.discountAmount = amount.min(subtotal); // Can't discount more than subtotal
        recalculateTotals();
    }

    /**
     * Clear any applied discount.
     */
    public void clearDiscount() {
        this.discountCode = null;
        this.discountAmount = BigDecimal.ZERO;
        recalculateTotals();
    }

    /**
     * Confirm the purchase.
     * Changes status from PENDING to CONFIRMED.
     */
    public void confirm() {
        if (this.status != PurchaseStatus.PENDING) {
            throw new IllegalStateException("Can only confirm pending purchases");
        }
        this.status = PurchaseStatus.CONFIRMED;
    }

    /**
     * Cancel the purchase.
     * Can only cancel if not yet shipped.
     */
    public void cancel() {
        if (this.status == PurchaseStatus.SHIPPED || 
            this.status == PurchaseStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel shipped or delivered purchases");
        }
        this.status = PurchaseStatus.CANCELLED;
    }

    /**
     * Check if purchase can be modified.
     */
    public boolean isModifiable() {
        return this.status == PurchaseStatus.PENDING;
    }

    /**
     * Check if purchase is completed.
     */
    public boolean isCompleted() {
        return this.status == PurchaseStatus.DELIVERED;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
        recalculateTotals();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "orderNumber='" + orderNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", totalItems=" + totalItems +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}
