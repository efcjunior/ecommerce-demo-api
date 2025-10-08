package demo.ecommerce.usecase.order;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderItem;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.OrderRepositoryGateway;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    private ProductRepositoryGateway productRepository;
    private OrderRepositoryGateway orderRepository;
    private CreateOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepositoryGateway.class);
        orderRepository = mock(OrderRepositoryGateway.class);
        useCase = new CreateOrderUseCaseImpl(productRepository, orderRepository);
    }

    @Test
    void shouldCreateOrderSuccessfullyWhenStockIsAvailable() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = new Product(productId, "Phone", "Desc", BigDecimal.valueOf(1000),
                "Electronics", 10, LocalDateTime.now(), LocalDateTime.now());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        CreateOrderInput input = new CreateOrderInput(
                userId,
                List.of(new OrderItemInput(productId, 2))
        );

        Order savedOrder = new Order(UUID.randomUUID(), userId, List.of(
                new OrderItem(productId, 2, BigDecimal.valueOf(1000))
        ), BigDecimal.valueOf(2000), OrderStatus.PENDING, LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CreateOrderOutput output = useCase.execute(input);

        assertNotNull(output);
        assertEquals(savedOrder.getId(), output.orderId());
        assertEquals(OrderStatus.PENDING, output.status());
        assertEquals(BigDecimal.valueOf(2000), output.totalAmount());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldCancelOrderWhenStockIsInsufficient() {
        // arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        var product = new Product(
                productId,
                "Product A",
                "Description",
                BigDecimal.valueOf(100),
                "Category",
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        var itemInput = new OrderItemInput(productId, 5);
        var input = new CreateOrderInput(userId, List.of(itemInput));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var useCase = new CreateOrderUseCaseImpl(productRepository, orderRepository);

        // act
        var output = useCase.execute(input);

        // assert
        assertEquals(OrderStatus.CANCELED, output.status());
        assertNotNull(output.orderId());
        assertTrue(output.totalAmount().compareTo(BigDecimal.ZERO) > 0);
    }

}