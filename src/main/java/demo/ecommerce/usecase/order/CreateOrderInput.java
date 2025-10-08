package demo.ecommerce.usecase.order;

import java.util.List;
import java.util.UUID;

public record CreateOrderInput(UUID userId, List<OrderItemInput> items) {}