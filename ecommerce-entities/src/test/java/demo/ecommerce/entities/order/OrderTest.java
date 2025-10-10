package demo.ecommerce.entities.order;

import demo.ecommerce.entities.exceptions.EmptyOrderException;
import demo.ecommerce.entities.exceptions.InvalidOrderStateException;
import demo.ecommerce.entities.exceptions.InvalidOrderTotalException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderWithValidItems() {
        OrderItem item1 = mockItem(new BigDecimal("100.00"), 2);
        OrderItem item2 = mockItem(new BigDecimal("200.00"), 1);

        Order order = Order.create(UUID.randomUUID(), List.of(item1, item2));

        assertNotNull(order.getId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(0, order.getTotalAmount().compareTo(new BigDecimal("400.00")));
        assertEquals(2, order.getItems().size());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithEmptyItems() {
        UUID userId = UUID.randomUUID();

        assertThrows(EmptyOrderException.class, () ->
                Order.create(userId, List.of())
        );
    }

    @Test
    void shouldCreateOrderWithCanceledStatusWhenAnyItemOutOfStock() {
        UUID userId = UUID.randomUUID();

        OrderItem inStock = mockItem(new BigDecimal("100.00"), 1);
        OrderItem outOfStock = mockOutOfStockItem(new BigDecimal("200.00"), 2);

        Order order = Order.create(userId, List.of(inStock, outOfStock));

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        assertEquals(0, order.getTotalAmount().compareTo(new BigDecimal("500.00")));
    }

    @Test
    void shouldPayOrderWhenStatusIsPending() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("150.00"), 2);

        Order order = Order.create(userId, List.of(item));

        order.pay();

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPayingNonPendingOrder() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("100.00"), 1);

        Order order = Order.create(userId, List.of(item));
        order.cancel();

        assertThrows(InvalidOrderStateException.class, order::pay);
    }

    @Test
    void shouldCancelOrderWhenPending() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("120.00"), 1);

        Order order = Order.create(userId, List.of(item));

        order.cancel();

        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }
    @Test
    void shouldThrowExceptionWhenCancelingPaidOrder() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("200.00"), 1);

        Order order = Order.create(userId, List.of(item));
        order.pay();

        assertThrows(InvalidOrderStateException.class, order::cancel);
    }

    @Test
    void shouldAddItemWhenOrderIsPending() {
        UUID userId = UUID.randomUUID();
        OrderItem item1 = mockItem(new BigDecimal("100.00"), 1);
        Order order = Order.create(userId, List.of(item1));

        OrderItem item2 = mockItem(new BigDecimal("50.00"), 2);
        order.addItem(item2);

        assertEquals(2, order.getItems().size());
        assertEquals(0, order.getTotalAmount().compareTo(new BigDecimal("200.00")));
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenAddingItemToNonPendingOrder() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("100.00"), 1);

        Order order = Order.create(userId, List.of(item));
        order.cancel();

        OrderItem newItem = mockItem(new BigDecimal("50.00"), 1);

        assertThrows(InvalidOrderStateException.class, () -> order.addItem(newItem));
    }

    @Test
    void shouldRecalculateTotalWhenItemsAdded() {
        UUID userId = UUID.randomUUID();
        OrderItem item1 = mockItem(new BigDecimal("100.00"), 1);
        OrderItem item2 = mockItem(new BigDecimal("50.00"), 2);

        Order order = Order.create(userId, List.of(item1));
        BigDecimal initialTotal = order.getTotalAmount();

        order.addItem(item2);

        assertEquals(0, order.getTotalAmount().compareTo(new BigDecimal("200.00"))); // 100 + (50*2)
        assertTrue(order.getTotalAmount().compareTo(initialTotal) > 0);
    }

    @Test
    void shouldThrowExceptionWhenTotalAmountIsNegative() {
        UUID userId = UUID.randomUUID();

        OrderItem invalidItem = new OrderItem(UUID.randomUUID(), 1, new BigDecimal("-10.00"), 100);

        assertThrows(InvalidOrderTotalException.class, () ->
                Order.create(userId, List.of(invalidItem))
        );
    }

    @Test
    void shouldPreserveIdUserIdAndCreatedAt() {
        UUID userId = UUID.randomUUID();
        OrderItem item = mockItem(new BigDecimal("100.00"), 1);

        Order order = Order.create(userId, List.of(item));

        UUID originalId = order.getId();
        UUID originalUserId = order.getUserId();
        var originalCreatedAt = order.getCreatedAt();

        order.addItem(mockItem(new BigDecimal("50.00"), 1));
        order.pay();

        assertEquals(originalId, order.getId());
        assertEquals(originalUserId, order.getUserId());
        assertEquals(originalCreatedAt, order.getCreatedAt());
    }

    private OrderItem mockItem(BigDecimal price, int qty) {
        return new OrderItem(UUID.randomUUID(), qty, price, 100);
    }

    private OrderItem mockOutOfStockItem(BigDecimal price, int qty) {
        return new OrderItem(UUID.randomUUID(), qty, price, 0);
    }

}
