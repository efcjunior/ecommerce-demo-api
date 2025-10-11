package demo.ecommerce.usecases.product.command.createproduct;

import java.math.BigDecimal;

public record CreateProductInput(
        String name,
        String description,
        BigDecimal price,
        String category,
        int stockQuantity
) {}
