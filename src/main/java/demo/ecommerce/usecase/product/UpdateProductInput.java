package demo.ecommerce.usecase.product;

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