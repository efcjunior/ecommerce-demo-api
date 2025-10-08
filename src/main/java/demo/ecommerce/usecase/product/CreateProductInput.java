package demo.ecommerce.usecase.product;

import java.math.BigDecimal;

public record CreateProductInput(
        String name,
        String description,
        BigDecimal price,
        String category,
        int stockQuantity
) {}