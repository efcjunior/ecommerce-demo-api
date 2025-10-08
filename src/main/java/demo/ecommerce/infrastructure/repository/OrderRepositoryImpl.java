package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.Order;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryGateway {

    private final SpringDataOrderRepository repository;

    public OrderRepositoryImpl(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        var entity = OrderEntity.fromDomain(order);
        var saved = repository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository.findById(id).map(OrderEntity::toDomain);
    }
}