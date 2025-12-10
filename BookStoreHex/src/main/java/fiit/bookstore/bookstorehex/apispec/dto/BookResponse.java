package fiit.bookstore.bookstorehex.apispec.dto;

import fiit.bookstore.bookstorehex.domain.entity.Book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for book response.
 * Part of the API specification layer.
 */
public class BookResponse {

    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String publisher;
    private Integer publicationYear;
    private String genre;
    private String description;
    private Integer quantity;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public BookResponse() {
    }

    /**
     * Factory method to create BookResponse from Book domain entity.
     */
    public static BookResponse fromEntity(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setPrice(book.getPrice());
        response.setPublisher(book.getPublisher());
        response.setPublicationYear(book.getPublicationYear());
        response.setGenre(book.getGenre());
        response.setDescription(book.getDescription());
        response.setQuantity(book.getQuantity());
        response.setAvailable(book.isAvailable());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
