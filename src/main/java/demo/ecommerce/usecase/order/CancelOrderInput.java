package demo.ecommerce.usecase.order;

import java.util.UUID;

public record CancelOrderInput(
        UUID orderId,
        String reason
) {}
