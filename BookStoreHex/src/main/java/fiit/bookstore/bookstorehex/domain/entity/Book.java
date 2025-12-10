package fiit.bookstore.bookstorehex.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Book entity representing a book in the bookstore.
 * In hexagonal architecture, the domain is framework-agnostic.
 */
public class Book extends BaseEntity {

    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String publisher;
    private Integer publicationYear;
    private String genre;
    private String description;
    private Integer quantity = 0;

    // Default constructor
    public Book() {
        super();
    }

    // Full constructor
    public Book(String title, String author, String isbn, BigDecimal price) {
        super();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = 0;
    }

    // Builder pattern for flexible object creation
    public static BookBuilder builder() {
        return new BookBuilder();
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

    // Business logic methods (Encapsulation of behavior)
    public boolean isAvailable() {
        return this.quantity != null && this.quantity > 0;
    }

    /**
     * Check if the book has sufficient quantity for purchase.
     * @param amount the amount to check
     * @return true if sufficient quantity is available
     */
    public boolean hasStock(int amount) {
        return this.quantity != null && this.quantity >= amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity - amount < 0) {
            throw new IllegalStateException("Insufficient quantity available");
        }
        this.quantity -= amount;
    }

    /**
     * Alias for decreaseQuantity - reduces stock when books are purchased.
     */
    public void reduceQuantity(int amount) {
        decreaseQuantity(amount);
    }

    public void increaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
    }

    /**
     * Alias for increaseQuantity - adds stock when order is cancelled or books are restocked.
     */
    public void addQuantity(int amount) {
        increaseQuantity(amount);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isbn);
    }

    /**
     * Builder class for Book entity.
     */
    public static class BookBuilder {
        private String title;
        private String author;
        private String isbn;
        private BigDecimal price;
        private String publisher;
        private Integer publicationYear;
        private String genre;
        private String description;
        private Integer quantity = 0;

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder author(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public BookBuilder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookBuilder publicationYear(Integer publicationYear) {
            this.publicationYear = publicationYear;
            return this;
        }

        public BookBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public BookBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BookBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Book build() {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setPrice(price);
            book.setPublisher(publisher);
            book.setPublicationYear(publicationYear);
            book.setGenre(genre);
            book.setDescription(description);
            book.setQuantity(quantity);
            return book;
        }
    }
}
