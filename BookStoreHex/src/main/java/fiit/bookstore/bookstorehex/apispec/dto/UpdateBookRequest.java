package fiit.bookstore.bookstorehex.apispec.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for updating an existing book.
 * All fields are optional - only provided fields will be updated.
 */
public class UpdateBookRequest {

    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(min = 1, max = 255, message = "Author name must be between 1 and 255 characters")
    private String author;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 255, message = "Publisher name cannot exceed 255 characters")
    private String publisher;

    @Min(value = 1450, message = "Publication year must be at least 1450")
    @Max(value = 2100, message = "Publication year cannot exceed 2100")
    private Integer publicationYear;

    @Size(max = 100, message = "Genre cannot exceed 100 characters")
    private String genre;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    // Default constructor
    public UpdateBookRequest() {
    }

    // Getters and Setters
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
}
