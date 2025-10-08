package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.gateway.OrderEventGateway;
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

class PayOrderUseCaseTest {

    private OrderRepositoryGateway orderRepository;
    private OrderEventGateway orderEventGateway;
    private PayOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepositoryGateway.class);
        orderEventGateway = mock(OrderEventGateway.class);
        useCase = new PayOrderUseCaseImpl(orderRepository, orderEventGateway);
    }

    @Test
    void shouldPayPendingOrderSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, UUID.randomUUID(), List.of(),
                BigDecimal.valueOf(2000), OrderStatus.PENDING, LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        PayOrderInput input = new PayOrderInput(orderId, "CREDIT_CARD");

        PayOrderOutput output = useCase.execute(input);

        assertEquals(orderId, output.orderId());
        assertEquals(OrderStatus.PAID, output.status());
        assertNotNull(output.paidAt());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
        verify(orderEventGateway).publishOrderPaid(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        PayOrderInput input = new PayOrderInput(orderId, "CREDIT_CARD");

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
        verify(orderEventGateway, never()).publishOrderPaid(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderIsAlreadyPaid() {
        UUID orderId = UUID.randomUUID();
        Order paidOrder = new Order(orderId, UUID.randomUUID(), List.of(),
                BigDecimal.valueOf(1000), OrderStatus.PAID, LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(paidOrder));

        PayOrderInput input = new PayOrderInput(orderId, "PIX");

        assertThrows(IllegalStateException.class, () -> useCase.execute(input));
        verify(orderEventGateway, never()).publishOrderPaid(any());
    }
}