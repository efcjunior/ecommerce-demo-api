package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderItem;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final ProductRepositoryGateway productRepository;
    private final OrderRepositoryGateway orderRepository;

    public CreateOrderUseCaseImpl(ProductRepositoryGateway productRepository,
                                  OrderRepositoryGateway orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public CreateOrderOutput execute(CreateOrderInput input) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        boolean insufficientStock = false;

        for (OrderItemInput itemInput : input.items()) {
            var productOpt = productRepository.findById(itemInput.productId());
            if (productOpt.isEmpty()) {
                insufficientStock = true;
                continue;
            }

            var product = productOpt.get();
            if (product.getStockQuantity() < itemInput.quantity()) {
                insufficientStock = true;
            }

            var item = new OrderItem(product.getId(), itemInput.quantity(), product.getPrice());
            items.add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemInput.quantity())));
        }

        var order = new Order(
                UUID.randomUUID(),
                input.userId(),
                items,
                total,
                insufficientStock ? OrderStatus.CANCELED : OrderStatus.PENDING,
                LocalDateTime.now()
        );

        var saved = orderRepository.save(order);

        return new CreateOrderOutput(saved.getId(), saved.getTotalAmount(), saved.getStatus());
    }
}
