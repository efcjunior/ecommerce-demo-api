package demo.ecommerce.gateway;

import demo.ecommerce.entity.Order;
import java.util.UUID;
import java.util.Optional;

public interface OrderRepositoryGateway {
    Order save(Order order);
    Optional<Order> findById(UUID id);
}