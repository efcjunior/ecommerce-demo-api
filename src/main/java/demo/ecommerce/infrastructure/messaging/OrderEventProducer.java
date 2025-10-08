package demo.ecommerce.infrastructure.messaging;

import demo.ecommerce.gateway.OrderEventGateway;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventProducer implements OrderEventGateway {

    private static final String TOPIC = "order.paid";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderPaid(UUID orderId) {
        kafkaTemplate.send(TOPIC, orderId.toString());
    }
}
