package demo.ecommerce.usecases.product.command.updateproduct;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductInput(
        UUID productId,
        String name,
        String description,
        BigDecimal price,
        String category,
        int stockQuantity
) {}
