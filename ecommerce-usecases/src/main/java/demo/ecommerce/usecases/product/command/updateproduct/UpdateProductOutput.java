package demo.ecommerce.usecases.product.command.updateproduct;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateProductOutput(
        UUID productId,
        LocalDateTime updatedAt
) {}
