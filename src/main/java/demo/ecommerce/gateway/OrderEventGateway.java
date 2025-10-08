package demo.ecommerce.gateway;

import java.util.UUID;

public interface OrderEventGateway {
    void publishOrderPaid(UUID orderId);
}