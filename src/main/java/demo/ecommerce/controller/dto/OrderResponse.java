package demo.ecommerce.controller.dto;

import demo.ecommerce.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        BigDecimal totalAmount,
        OrderStatus status
) {}