package demo.ecommerce.entities.product;

import demo.ecommerce.entities.exceptions.InsufficientStockException;
import demo.ecommerce.entities.exceptions.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Product {

    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int stockQuantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Product(UUID id,
                    String name,
                    String description,
                    BigDecimal price,
                    String category,
                    int stockQuantity,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        validate(name, price, stockQuantity);
        this.id = id;
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(String name, String description, BigDecimal price, String category, int stockQuantity) {
        return new Product(
                UUID.randomUUID(),
                name,
                description,
                price,
                category,
                stockQuantity,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void update(String name, String description, BigDecimal price, String category, int stockQuantity) {
        validate(name, price, stockQuantity);
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) throw new ValidationException("Quantity must be greater than zero");
        if (stockQuantity < quantity) throw new InsufficientStockException();
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) throw new ValidationException("Quantity must be greater than zero");
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    private void validate(String name, BigDecimal price, int stockQuantity) {
        if (name == null || name.isBlank())
            throw new ValidationException("Product name cannot be blank");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Price must be greater than zero");
        if (stockQuantity < 0)
            throw new ValidationException("Stock quantity cannot be negative");
    }
}
