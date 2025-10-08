package demo.ecommerce.usecase.order;

import java.util.UUID;

public record PayOrderInput(
        UUID orderId,
        String paymentMethod
) {}