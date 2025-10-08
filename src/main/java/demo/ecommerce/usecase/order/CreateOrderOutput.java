package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderOutput(
        UUID orderId,
        BigDecimal totalAmount,
        OrderStatus status
) {}