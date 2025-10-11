package demo.ecommerce.usecases.product.command.createproduct;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductOutput(
        UUID productId,
        LocalDateTime createdAt
) {}
