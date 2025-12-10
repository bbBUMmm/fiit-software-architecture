package fiit.bookstore.bookstorehex.apispec.dto;

import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO for purchase response.
 */
public class PurchaseResponse {

    private UUID id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    private List<PurchaseItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private String discountCode;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private PurchaseStatus status;
    private LocalDateTime purchaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PurchaseResponse() {}

    public static PurchaseResponse fromEntity(Purchase purchase) {
        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setOrderNumber(purchase.getOrderNumber());
        response.setCustomerName(purchase.getCustomerName());
        response.setCustomerEmail(purchase.getCustomerEmail());
        response.setShippingAddress(purchase.getShippingAddress());
        response.setItems(purchase.getItems().stream()
                .map(PurchaseItemResponse::fromEntity)
                .collect(Collectors.toList()));
        response.setSubtotal(purchase.getSubtotal());
        response.setDiscountAmount(purchase.getDiscountAmount());
        response.setDiscountCode(purchase.getDiscountCode());
        response.setTotalAmount(purchase.getTotalAmount());
        response.setTotalItems(purchase.getTotalItems());
        response.setStatus(purchase.getStatus());
        response.setPurchaseDate(purchase.getPurchaseDate());
        response.setCreatedAt(purchase.getCreatedAt());
        response.setUpdatedAt(purchase.getUpdatedAt());
        return response;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<PurchaseItemResponse> getItems() { return items; }
    public void setItems(List<PurchaseItemResponse> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public PurchaseStatus getStatus() { return status; }
    public void setStatus(PurchaseStatus status) { this.status = status; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
