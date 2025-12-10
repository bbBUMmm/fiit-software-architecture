package fiit.bookstore.bookstorelayeredas.presentation.controller;

import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseResponse;
import fiit.bookstore.bookstorelayeredas.business.service.PurchaseService;
import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseStatus;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Purchase operations (buying books).
 * Demonstrates:
 * - Presentation Layer: Handles HTTP requests and responses
 * - Rich domain operations beyond CRUD
 * - RESTful API design for e-commerce operations
 */
@RestController
@RequestMapping("/api/v1/purchases")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Create a new purchase (buy books).
     * POST /api/v1/purchases
     * 
     * This is the main endpoint for buying books. It:
     * - Validates stock availability
     * - Applies discounts if provided
     * - Reduces book inventory
     * - Creates the purchase record
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody PurchaseRequest request) {
        logger.info("Received request to create purchase for: {}", request.getCustomerEmail());
        PurchaseResponse response = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a purchase by ID.
     * GET /api/v1/purchases/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable UUID id) {
        logger.info("Received request to get purchase with ID: {}", id);
        PurchaseResponse response = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a purchase by order number.
     * GET /api/v1/purchases/order/{orderNumber}
     */
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PurchaseResponse> getPurchaseByOrderNumber(@PathVariable String orderNumber) {
        logger.info("Received request to get purchase with order number: {}", orderNumber);
        PurchaseResponse response = purchaseService.getPurchaseByOrderNumber(orderNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all purchases.
     * GET /api/v1/purchases
     */
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        logger.info("Received request to get all purchases");
        List<PurchaseResponse> response = purchaseService.getAllPurchases();
        return ResponseEntity.ok(response);
    }

    /**
     * Get purchases by customer email.
     * GET /api/v1/purchases/customer?email={email}
     */
    @GetMapping("/customer")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByCustomerEmail(@RequestParam String email) {
        logger.info("Received request to get purchases for customer: {}", email);
        List<PurchaseResponse> response = purchaseService.getPurchasesByCustomerEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Get purchases by status.
     * GET /api/v1/purchases/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByStatus(@PathVariable PurchaseStatus status) {
        logger.info("Received request to get purchases with status: {}", status);
        List<PurchaseResponse> response = purchaseService.getPurchasesByStatus(status);
        return ResponseEntity.ok(response);
    }

    /**
     * Confirm a pending purchase.
     * POST /api/v1/purchases/{id}/confirm
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<PurchaseResponse> confirmPurchase(@PathVariable UUID id) {
        logger.info("Received request to confirm purchase: {}", id);
        PurchaseResponse response = purchaseService.confirmPurchase(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel a purchase (restores inventory).
     * POST /api/v1/purchases/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseResponse> cancelPurchase(@PathVariable UUID id) {
        logger.info("Received request to cancel purchase: {}", id);
        PurchaseResponse response = purchaseService.cancelPurchase(id);
        return ResponseEntity.ok(response);
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
        PurchaseResponse response = purchaseService.updatePurchaseStatus(id, status);
        return ResponseEntity.ok(response);
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
        PurchaseResponse response = purchaseService.applyDiscountCode(id, code);
        return ResponseEntity.ok(response);
    }

    /**
     * Get purchase statistics.
     * GET /api/v1/purchases/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<PurchaseService.PurchaseStatistics> getPurchaseStatistics() {
        logger.info("Received request to get purchase statistics");
        PurchaseService.PurchaseStatistics statistics = purchaseService.getPurchaseStatistics();
        return ResponseEntity.ok(statistics);
    }
}
