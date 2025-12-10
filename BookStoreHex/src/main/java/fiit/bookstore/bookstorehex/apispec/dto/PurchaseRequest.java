package fiit.bookstore.bookstorehex.apispec.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for creating a purchase request (buying books).
 */
public class PurchaseRequest {

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PurchaseItemRequest> items;

    private String discountCode;

    public PurchaseRequest() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<PurchaseItemRequest> getItems() { return items; }
    public void setItems(List<PurchaseItemRequest> items) { this.items = items; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }
}
