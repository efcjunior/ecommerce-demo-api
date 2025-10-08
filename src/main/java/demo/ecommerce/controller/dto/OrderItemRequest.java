package demo.ecommerce.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record OrderItemRequest(
        @NotNull UUID productId,
        @Positive int quantity
) {}
