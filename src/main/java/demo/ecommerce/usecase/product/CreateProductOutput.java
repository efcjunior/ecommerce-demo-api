package demo.ecommerce.usecase.product;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductOutput(
        UUID productId,
        LocalDateTime createdAt
) {}