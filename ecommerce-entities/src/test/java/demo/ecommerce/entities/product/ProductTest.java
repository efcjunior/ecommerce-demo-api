package demo.ecommerce.entities.product;

import demo.ecommerce.entities.exceptions.InsufficientStockException;
import demo.ecommerce.entities.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWhenValidData() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                10
        );

        assertNotNull(product.getId());
        assertEquals("Gaming Laptop", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(10, product.getStockQuantity());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(ValidationException.class, () ->
                Product.create(
                        "   ", // blank name
                        "RTX 4060, 16GB RAM",
                        new BigDecimal("7999.90"),
                        "Electronics",
                        10
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        // zero
        assertThrows(ValidationException.class, () ->
                Product.create(
                        "Gaming Laptop",
                        "RTX 4060, 16GB RAM",
                        BigDecimal.ZERO,
                        "Electronics",
                        10
                )
        );

        // negative
        assertThrows(ValidationException.class, () ->
                Product.create(
                        "Gaming Laptop",
                        "RTX 4060, 16GB RAM",
                        new BigDecimal("-1.00"),
                        "Electronics",
                        10
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        assertThrows(ValidationException.class, () ->
                Product.create(
                        "Gaming Laptop",
                        "RTX 4060, 16GB RAM",
                        new BigDecimal("7999.90"),
                        "Electronics",
                        -5
                )
        );
    }

    @Test
    void shouldUpdateProductWhenValidData() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                10
        );

        LocalDateTime oldUpdatedAt = product.getUpdatedAt();

        product.update(
                "Gaming Laptop Pro",
                "RTX 4070, 32GB RAM",
                new BigDecimal("9999.90"),
                "Electronics",
                5
        );

        assertEquals("Gaming Laptop Pro", product.getName());
        assertEquals("RTX 4070, 32GB RAM", product.getDescription());
        assertEquals(new BigDecimal("9999.90"), product.getPrice());
        assertEquals(5, product.getStockQuantity());
        assertTrue(product.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidData() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                10
        );

        assertAll(
                // blank name
                () -> assertThrows(ValidationException.class, () ->
                        product.update("  ", "RTX 4070", new BigDecimal("8999.90"), "Electronics", 5)
                ),
                // invalid price
                () -> assertThrows(ValidationException.class, () ->
                        product.update("Gaming Laptop", "RTX 4070", BigDecimal.ZERO, "Electronics", 5)
                ),
                // negative stock
                () -> assertThrows(ValidationException.class, () ->
                        product.update("Gaming Laptop", "RTX 4070", new BigDecimal("8999.90"), "Electronics", -1)
                )
        );
    }

    @Test
    void shouldDecreaseStockWhenQuantityValid() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                10
        );

        LocalDateTime oldUpdatedAt = product.getUpdatedAt();

        product.decreaseStock(3);

        assertEquals(7, product.getStockQuantity());
        assertTrue(product.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void shouldThrowExceptionWhenDecreaseStockBelowZero() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                2
        );

        assertThrows(InsufficientStockException.class, () ->
                product.decreaseStock(5)
        );
    }

    @Test
    void shouldThrowExceptionWhenDecreaseStockWithZeroOrNegativeQuantity() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                5
        );

        assertAll(
                () -> assertThrows(ValidationException.class, () -> product.decreaseStock(0)),
                () -> assertThrows(ValidationException.class, () -> product.decreaseStock(-2))
        );
    }

    @Test
    void shouldIncreaseStockWhenQuantityValid() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                5
        );

        LocalDateTime oldUpdatedAt = product.getUpdatedAt();

        product.increaseStock(3);

        assertEquals(8, product.getStockQuantity());
        assertTrue(product.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void shouldThrowExceptionWhenIncreaseStockWithZeroOrNegativeQuantity() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                5
        );

        assertAll(
                () -> assertThrows(ValidationException.class, () -> product.increaseStock(0)),
                () -> assertThrows(ValidationException.class, () -> product.increaseStock(-1))
        );
    }

    @Test
    void shouldTrimNameOnCreateAndUpdate() {
        Product product = Product.create(
                "  Gaming Laptop  ",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                5
        );

        assertEquals("Gaming Laptop", product.getName());

        product.update("  Laptop Pro  ", "RTX 4070", new BigDecimal("9999.90"), "Electronics", 5);
        assertEquals("Laptop Pro", product.getName());
    }

    @Test
    void shouldPreserveIdAndCreatedAtAfterUpdate() {
        Product product = Product.create(
                "Gaming Laptop",
                "RTX 4060, 16GB RAM",
                new BigDecimal("7999.90"),
                "Electronics",
                5
        );

        UUID originalId = product.getId();
        LocalDateTime createdAt = product.getCreatedAt();

        product.update("Laptop Pro", "RTX 4070", new BigDecimal("9999.90"), "Electronics", 5);

        assertEquals(originalId, product.getId());
        assertEquals(createdAt, product.getCreatedAt());
    }
}
