package demo.ecommerce.infrastructure.messaging;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderItem;
import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class OrderEventConsumer {

    private final OrderRepositoryGateway orderRepository;
    private final ProductRepositoryGateway productRepository;
    private final ProductSearchGateway productSearch;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // armazenamento in-memory simples para idempotência
    private final Set<UUID> processedOrders = new HashSet<>();

    public OrderEventConsumer(OrderRepositoryGateway orderRepository,
                              ProductRepositoryGateway productRepository,
                              ProductSearchGateway productSearch,
                              KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productSearch = productSearch;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order.paid", groupId = "ecommerce-group")
    @Transactional
    public void handleOrderPaid(ConsumerRecord<String, String> record) {
        try {
            UUID orderId = UUID.fromString(record.value());

            // ✅ idempotência: ignora se já processado
            if (!processedOrders.add(orderId)) {
                return;
            }

            orderRepository.findById(orderId).ifPresent(this::updateStockForOrder);

        } catch (Exception ex) {
            kafkaTemplate.send("order.paid.dlq", record.value());
            System.err.println("❌ Erro ao processar evento " + record.value() + ": " + ex.getMessage());
        }
    }

    private void updateStockForOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            productRepository.findById(item.getProductId()).ifPresent(product -> {
                int newStock = Math.max(0, product.getStockQuantity() - item.getQuantity());

                var updated = new Product(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        newStock,
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                );

                productRepository.save(updated);
                productSearch.index(updated);
            });
        }
    }
}
