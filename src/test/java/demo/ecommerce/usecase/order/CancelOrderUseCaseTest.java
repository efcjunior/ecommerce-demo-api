package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelOrderUseCaseTest {

    private OrderRepositoryGateway orderRepository;
    private CancelOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepositoryGateway.class);
        useCase = new CancelOrderUseCaseImpl(orderRepository);
    }

    @Test
    void shouldCancelPendingOrderSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Order existing = new Order(orderId, UUID.randomUUID(), List.of(),
                BigDecimal.valueOf(500), OrderStatus.PENDING, LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existing));

        CancelOrderInput input = new CancelOrderInput(orderId, "Customer request");

        CancelOrderOutput output = useCase.execute(input);

        assertEquals(orderId, output.orderId());
        assertEquals(OrderStatus.CANCELED, output.status());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CancelOrderInput input = new CancelOrderInput(orderId, "Invalid ID");

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderAlreadyPaid() {
        UUID orderId = UUID.randomUUID();
        Order paid = new Order(orderId, UUID.randomUUID(), List.of(),
                BigDecimal.valueOf(1000), OrderStatus.PAID, LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(paid));

        CancelOrderInput input = new CancelOrderInput(orderId, "Customer regret");

        assertThrows(IllegalStateException.class, () -> useCase.execute(input));
        verify(orderRepository, never()).save(any());
    }
}