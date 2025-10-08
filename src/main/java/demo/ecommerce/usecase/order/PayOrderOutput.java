package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PayOrderOutput(
        UUID orderId,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime paidAt
) {}