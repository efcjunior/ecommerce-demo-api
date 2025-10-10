package demo.ecommerce.entities.order;

import demo.ecommerce.entities.exceptions.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record OrderItem(UUID productId, int quantity, BigDecimal price, int availableStock) {

    public OrderItem(UUID productId, int quantity, BigDecimal price, int availableStock) {
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.quantity = quantity;
        this.availableStock = availableStock;
        validate();
    }


    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isOutOfStock() {
        return quantity > availableStock;
    }

    private void validate() {
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException();
        }
    }
}
