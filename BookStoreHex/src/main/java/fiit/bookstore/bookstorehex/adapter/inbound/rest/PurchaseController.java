package fiit.bookstore.bookstorehex.adapter.inbound.rest;

import fiit.bookstore.bookstorehex.apispec.dto.PurchaseItemRequest;
import fiit.bookstore.bookstorehex.apispec.dto.PurchaseRequest;
import fiit.bookstore.bookstorehex.apispec.dto.PurchaseResponse;
import fiit.bookstore.bookstorehex.domain.entity.Book;
import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseItem;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;
import fiit.bookstore.bookstorehex.domain.port.inbound.PurchaseUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Purchase operations (buying books).
 * This is an inbound adapter in hexagonal architecture.
 * It adapts HTTP requests to the domain's PurchaseUseCase port.
 */
@RestController
@RequestMapping("/api/v1/purchases")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseUseCase purchaseUseCase;

    public PurchaseController(PurchaseUseCase purchaseUseCase) {
        this.purchaseUseCase = purchaseUseCase;
    }

    /**
     * Create a new purchase (buy books).
     * POST /api/v1/purchases
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody PurchaseRequest request) {
        logger.info("Received request to create purchase for: {}", request.getCustomerEmail());

        Purchase purchase = mapToEntity(request);
        Purchase createdPurchase = purchaseUseCase.createPurchase(purchase, request.getDiscountCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(PurchaseResponse.fromEntity(createdPurchase));
    }

    /**
     * Get a purchase by ID.
     * GET /api/v1/purchases/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable UUID id) {
        logger.info("Received request to get purchase with ID: {}", id);
        Purchase purchase = purchaseUseCase.getPurchaseById(id);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Get a purchase by order number.
     * GET /api/v1/purchases/order/{orderNumber}
     */
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PurchaseResponse> getPurchaseByOrderNumber(@PathVariable String orderNumber) {
        logger.info("Received request to get purchase with order number: {}", orderNumber);
        Purchase purchase = purchaseUseCase.getPurchaseByOrderNumber(orderNumber);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Get all purchases.
     * GET /api/v1/purchases
     */
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        logger.info("Received request to get all purchases");
        List<PurchaseResponse> response = purchaseUseCase.getAllPurchases().stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get purchases by customer email.
     * GET /api/v1/purchases/customer?email={email}
     */
    @GetMapping("/customer")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByCustomerEmail(@RequestParam String email) {
        logger.info("Received request to get purchases for customer: {}", email);
        List<PurchaseResponse> response = purchaseUseCase.getPurchasesByCustomerEmail(email).stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get purchases by status.
     * GET /api/v1/purchases/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByStatus(@PathVariable PurchaseStatus status) {
        logger.info("Received request to get purchases with status: {}", status);
        List<PurchaseResponse> response = purchaseUseCase.getPurchasesByStatus(status).stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Confirm a pending purchase.
     * POST /api/v1/purchases/{id}/confirm
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<PurchaseResponse> confirmPurchase(@PathVariable UUID id) {
        logger.info("Received request to confirm purchase: {}", id);
        Purchase purchase = purchaseUseCase.confirmPurchase(id);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Cancel a purchase (restores inventory).
     * POST /api/v1/purchases/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseResponse> cancelPurchase(@PathVariable UUID id) {
        logger.info("Received request to cancel purchase: {}", id);
        Purchase purchase = purchaseUseCase.cancelPurchase(id);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Update purchase status.
     * PATCH /api/v1/purchases/{id}/status?status={status}
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseResponse> updatePurchaseStatus(
            @PathVariable UUID id,
            @RequestParam PurchaseStatus status) {
        logger.info("Received request to update purchase {} to status: {}", id, status);
        Purchase purchase = purchaseUseCase.updatePurchaseStatus(id, status);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Apply a discount code to a pending purchase.
     * POST /api/v1/purchases/{id}/discount?code={code}
     */
    @PostMapping("/{id}/discount")
    public ResponseEntity<PurchaseResponse> applyDiscountCode(
            @PathVariable UUID id,
            @RequestParam String code) {
        logger.info("Received request to apply discount code '{}' to purchase: {}", code, id);
        Purchase purchase = purchaseUseCase.applyDiscountCode(id, code);
        return ResponseEntity.ok(PurchaseResponse.fromEntity(purchase));
    }

    /**
     * Get purchase statistics.
     * GET /api/v1/purchases/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<PurchaseUseCase.PurchaseStatistics> getPurchaseStatistics() {
        logger.info("Received request to get purchase statistics");
        PurchaseUseCase.PurchaseStatistics statistics = purchaseUseCase.getPurchaseStatistics();
        return ResponseEntity.ok(statistics);
    }

    // ==================== Mapping Methods ====================

    private Purchase mapToEntity(PurchaseRequest request) {
        Purchase purchase = new Purchase();
        purchase.setCustomerName(request.getCustomerName());
        purchase.setCustomerEmail(request.getCustomerEmail());
        purchase.setShippingAddress(request.getShippingAddress());

        // Map items
        for (PurchaseItemRequest itemRequest : request.getItems()) {
            Book book = new Book();
            book.setId(itemRequest.getBookId());

            PurchaseItem item = new PurchaseItem();
            item.setBook(book);
            item.setQuantity(itemRequest.getQuantity());

            purchase.addItem(item);
        }

        return purchase;
    }
}

