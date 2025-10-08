package demo.ecommerce.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String category,
        int stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
