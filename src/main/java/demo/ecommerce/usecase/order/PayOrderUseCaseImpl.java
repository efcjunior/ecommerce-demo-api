package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.gateway.OrderEventGateway;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PayOrderUseCaseImpl implements PayOrderUseCase {

    private final OrderRepositoryGateway orderRepository;
    private final OrderEventGateway orderEventGateway;

    public PayOrderUseCaseImpl(OrderRepositoryGateway orderRepository,
                               OrderEventGateway orderEventGateway) {
        this.orderRepository = orderRepository;
        this.orderEventGateway = orderEventGateway;
    }

    @Override
    public PayOrderOutput execute(PayOrderInput input) {
        UUID orderId = input.orderId();
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (existing.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Order already paid");
        }

        Order updated = new Order(
                existing.getId(),
                existing.getUserId(),
                existing.getItems(),
                existing.getTotalAmount(),
                OrderStatus.PAID,
                existing.getCreatedAt()
        );

        orderRepository.save(updated);
        orderEventGateway.publishOrderPaid(orderId);

        return new PayOrderOutput(
                updated.getId(),
                updated.getTotalAmount(),
                updated.getStatus(),
                LocalDateTime.now()
        );
    }
}