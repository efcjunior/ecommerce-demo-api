package demo.ecommerce.usecase.order;

import java.util.UUID;

public record OrderItemInput(UUID productId, int quantity) {}