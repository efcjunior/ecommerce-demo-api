package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private final OrderRepositoryGateway orderRepository;

    public CancelOrderUseCaseImpl(OrderRepositoryGateway orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CancelOrderOutput execute(CancelOrderInput input) {
        UUID orderId = input.orderId();
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (existing.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Paid orders cannot be canceled");
        }

        Order canceled = new Order(
                existing.getId(),
                existing.getUserId(),
                existing.getItems(),
                existing.getTotalAmount(),
                OrderStatus.CANCELED,
                existing.getCreatedAt()
        );

        orderRepository.save(canceled);

        return new CancelOrderOutput(orderId, existing.getTotalAmount(), OrderStatus.CANCELED);
    }
}