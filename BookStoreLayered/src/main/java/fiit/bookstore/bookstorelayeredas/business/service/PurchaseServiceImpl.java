package fiit.bookstore.bookstorelayeredas.business.service;

import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseItemRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseResponse;
import fiit.bookstore.bookstorelayeredas.business.exception.*;
import fiit.bookstore.bookstorelayeredas.data.entity.Book;
import fiit.bookstore.bookstorelayeredas.data.entity.Purchase;
import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseItem;
import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseStatus;
import fiit.bookstore.bookstorelayeredas.data.repository.BookRepository;
import fiit.bookstore.bookstorelayeredas.data.repository.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of PurchaseService.
 * Demonstrates:
 * - Rich domain logic beyond CRUD
 * - Business rules enforcement (stock validation, discounts)
 * - Transaction management
 * - Inventory management
 * - State machine pattern for purchase status
 */
@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final PurchaseRepository purchaseRepository;
    private final BookRepository bookRepository;

    // Discount codes configuration (in real app, this would be in database)
    private static final Map<String, DiscountConfig> DISCOUNT_CODES = Map.of(
            "SAVE10", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(10)),
            "SAVE20", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(20)),
            "FLAT5", new DiscountConfig(DiscountType.FIXED, BigDecimal.valueOf(5)),
            "FLAT10", new DiscountConfig(DiscountType.FIXED, BigDecimal.valueOf(10)),
            "WELCOME", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(15)),
            "BOOKWORM", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(25))
    );

    // Constructor injection
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, BookRepository bookRepository) {
        this.purchaseRepository = purchaseRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        logger.info("Creating new purchase for customer: {}", request.getCustomerEmail());

        // Step 1: Validate and collect books
        Map<UUID, Book> bookMap = validateAndCollectBooks(request.getItems());

        // Step 2: Validate stock availability for all items
        validateStockAvailability(request.getItems(), bookMap);

        // Step 3: Create purchase entity
        Purchase purchase = new Purchase();
        purchase.setCustomerName(request.getCustomerName());
        purchase.setCustomerEmail(request.getCustomerEmail());
        purchase.setShippingAddress(request.getShippingAddress());

        // Step 4: Create purchase items and reduce inventory
        for (PurchaseItemRequest itemRequest : request.getItems()) {
            Book book = bookMap.get(itemRequest.getBookId());
            
            // Create purchase item
            PurchaseItem purchaseItem = new PurchaseItem(book, itemRequest.getQuantity());
            purchase.addItem(purchaseItem);
            
            // Reduce book inventory
            book.reduceQuantity(itemRequest.getQuantity());
            bookRepository.save(book);
            
            logger.debug("Added item: {} x {} (stock remaining: {})", 
                    book.getTitle(), itemRequest.getQuantity(), book.getQuantity());
        }

        // Step 5: Apply discount if provided
        applyDiscount(request, purchase);

        // Step 6: Save and return
        Purchase savedPurchase = purchaseRepository.save(purchase);
        logger.info("Purchase created successfully: {} (Total: {})", 
                savedPurchase.getOrderNumber(), savedPurchase.getTotalAmount());

        return PurchaseResponse.fromEntity(savedPurchase);
    }

    private void applyDiscount(PurchaseRequest request, Purchase purchase){
        if (request.getDiscountCode() != null && !request.getDiscountCode().isBlank()) {
            applyDiscount(purchase, request.getDiscountCode());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(UUID id) {
        logger.debug("Fetching purchase with ID: {}", id);
        
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
        
        return PurchaseResponse.fromEntity(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseByOrderNumber(String orderNumber) {
        logger.debug("Fetching purchase with order number: {}", orderNumber);
        
        Purchase purchase = purchaseRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new PurchaseNotFoundException(orderNumber));
        
        return PurchaseResponse.fromEntity(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesByCustomerEmail(String email) {
        logger.debug("Fetching purchases for customer: {}", email);
        
        return purchaseRepository.findByCustomerEmailIgnoreCase(email).stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesByStatus(PurchaseStatus status) {
        logger.debug("Fetching purchases with status: {}", status);
        
        return purchaseRepository.findByStatus(status).stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        logger.debug("Fetching all purchases");
        
        return purchaseRepository.findAll().stream()
                .map(PurchaseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseResponse confirmPurchase(UUID id) {
        logger.info("Confirming purchase with ID: {}", id);
        
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (purchase.getStatus() != PurchaseStatus.PENDING) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot confirm purchase in status: %s", purchase.getStatus()));
        }

        purchase.confirm();
        Purchase savedPurchase = purchaseRepository.save(purchase);
        
        logger.info("Purchase confirmed: {}", savedPurchase.getOrderNumber());
        return PurchaseResponse.fromEntity(savedPurchase);
    }

    @Override
    public PurchaseResponse cancelPurchase(UUID id) {
        logger.info("Cancelling purchase with ID: {}", id);
        
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (purchase.getStatus() == PurchaseStatus.SHIPPED || 
            purchase.getStatus() == PurchaseStatus.DELIVERED ||
            purchase.getStatus() == PurchaseStatus.CANCELLED) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot cancel purchase in status: %s", purchase.getStatus()));
        }

        // Restore inventory for all items
        for (PurchaseItem item : purchase.getItems()) {
            Book book = item.getBook();
            if (book != null) {
                book.addQuantity(item.getQuantity());
                bookRepository.save(book);
                logger.debug("Restored {} copies of '{}' to inventory", 
                        item.getQuantity(), book.getTitle());
            }
        }

        purchase.cancel();
        Purchase savedPurchase = purchaseRepository.save(purchase);
        
        logger.info("Purchase cancelled: {}", savedPurchase.getOrderNumber());
        return PurchaseResponse.fromEntity(savedPurchase);
    }

    @Override
    public PurchaseResponse updatePurchaseStatus(UUID id, PurchaseStatus status) {
        logger.info("Updating purchase {} to status: {}", id, status);
        
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        // Validate status transition
        validateStatusTransition(purchase.getStatus(), status);

        purchase.setStatus(status);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        
        logger.info("Purchase status updated: {} -> {}", 
                savedPurchase.getOrderNumber(), status);
        return PurchaseResponse.fromEntity(savedPurchase);
    }

    @Override
    public PurchaseResponse applyDiscountCode(UUID id, String discountCode) {
        logger.info("Applying discount code '{}' to purchase: {}", discountCode, id);
        
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (!purchase.isModifiable()) {
            throw new InvalidPurchaseStateException(
                    "Cannot apply discount to a non-pending purchase");
        }

        applyDiscount(purchase, discountCode);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        
        logger.info("Discount applied to purchase: {} (New total: {})", 
                savedPurchase.getOrderNumber(), savedPurchase.getTotalAmount());
        return PurchaseResponse.fromEntity(savedPurchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseStatistics getPurchaseStatistics() {
        logger.debug("Calculating purchase statistics");
        
        long total = purchaseRepository.count();
        long pending = purchaseRepository.countByStatus(PurchaseStatus.PENDING);
        long confirmed = purchaseRepository.countByStatus(PurchaseStatus.CONFIRMED);
        long cancelled = purchaseRepository.countByStatus(PurchaseStatus.CANCELLED);
        
        // Calculate total revenue from non-cancelled purchases
        BigDecimal revenue = purchaseRepository.findAll().stream()
                .filter(p -> p.getStatus() != PurchaseStatus.CANCELLED && 
                            p.getStatus() != PurchaseStatus.REFUNDED)
                .map(Purchase::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new PurchaseStatistics(total, pending, confirmed, cancelled, revenue);
    }

    // ==================== Private Helper Methods ====================

    /**
     * Validate that all books exist and collect them in a map.
     */
    private Map<UUID, Book> validateAndCollectBooks(List<PurchaseItemRequest> items) {
        Map<UUID, Book> bookMap = new HashMap<>();
        
        for (PurchaseItemRequest item : items) {
            if (!bookMap.containsKey(item.getBookId())) {
                Book book = bookRepository.findById(item.getBookId())
                        .orElseThrow(() -> new BookNotFoundException(item.getBookId()));
                bookMap.put(item.getBookId(), book);
            }
        }
        
        return bookMap;
    }

    /**
     * Validate stock availability for all items.
     * Throws InsufficientStockException if any item has insufficient stock.
     */
    private void validateStockAvailability(List<PurchaseItemRequest> items, Map<UUID, Book> bookMap) {
        // First, aggregate quantities for duplicate book IDs
        Map<UUID, Integer> requestedQuantities = new HashMap<>();
        for (PurchaseItemRequest item : items) {
            requestedQuantities.merge(item.getBookId(), item.getQuantity(), Integer::sum);
        }

        // Then validate stock for each book
        for (Map.Entry<UUID, Integer> entry : requestedQuantities.entrySet()) {
            Book book = bookMap.get(entry.getKey());
            int requested = entry.getValue();
            
            if (book.getQuantity() < requested) {
                throw new InsufficientStockException(
                        book.getIsbn(), requested, book.getQuantity());
            }
        }
    }

    /**
     * Apply discount to a purchase based on discount code.
     */
    private void applyDiscount(Purchase purchase, String discountCode) {
        String upperCode = discountCode.toUpperCase().trim();
        DiscountConfig config = DISCOUNT_CODES.get(upperCode);
        
        if (config == null) {
            throw new InvalidDiscountCodeException(discountCode);
        }

        if (config.type == DiscountType.PERCENTAGE) {
            purchase.applyPercentageDiscount(config.value, upperCode);
        } else {
            purchase.applyFixedDiscount(config.value, upperCode);
        }
        
        logger.debug("Applied discount: {} ({} {})", 
                upperCode, config.value, config.type);
    }

    /**
     * Validate that a status transition is allowed.
     */
    private void validateStatusTransition(PurchaseStatus current, PurchaseStatus target) {
        // Define valid transitions
        Map<PurchaseStatus, Set<PurchaseStatus>> validTransitions = Map.of(
                PurchaseStatus.PENDING, Set.of(PurchaseStatus.CONFIRMED, PurchaseStatus.CANCELLED),
                PurchaseStatus.CONFIRMED, Set.of(PurchaseStatus.PROCESSING, PurchaseStatus.CANCELLED),
                PurchaseStatus.PROCESSING, Set.of(PurchaseStatus.SHIPPED, PurchaseStatus.CANCELLED),
                PurchaseStatus.SHIPPED, Set.of(PurchaseStatus.DELIVERED),
                PurchaseStatus.DELIVERED, Set.of(PurchaseStatus.REFUNDED),
                PurchaseStatus.CANCELLED, Set.of(),
                PurchaseStatus.REFUNDED, Set.of()
        );

        Set<PurchaseStatus> allowed = validTransitions.getOrDefault(current, Set.of());
        if (!allowed.contains(target)) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot transition from %s to %s", current, target));
        }
    }

    private enum DiscountType {
        PERCENTAGE, FIXED
    }

    private record DiscountConfig(DiscountType type, BigDecimal value) {}
}
