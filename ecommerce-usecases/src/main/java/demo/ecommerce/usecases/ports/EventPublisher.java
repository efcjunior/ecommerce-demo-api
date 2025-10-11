package demo.ecommerce.usecases.ports;

public interface EventPublisher {
    void publish(Object event);
}