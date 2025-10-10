package demo.ecommerce.entities.order;

import demo.ecommerce.entities.exceptions.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@ToString
@EqualsAndHashCode
public class Order {

    private final UUID id;
    private final UUID userId;
    private final LocalDateTime createdAt;

    private final List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;

    private Order(UUID id, UUID userId, List<OrderItem> items,
                  BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.items = new ArrayList<>(Objects.requireNonNull(items, "Items cannot be null"));
        this.totalAmount = totalAmount;
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date cannot be null");
        validate();
    }

    public static Order create(UUID userId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new EmptyOrderException();
        }

        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean anyOutOfStock = items.stream().anyMatch(OrderItem::isOutOfStock);

        return new Order(
                UUID.randomUUID(),
                userId,
                items,
                total,
                anyOutOfStock ? OrderStatus.CANCELED : OrderStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public void pay() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Only pending orders can be paid");
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new InvalidOrderStateException("Paid orders cannot be canceled");
        }
        this.status = OrderStatus.CANCELED;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Cannot add items to a non-pending order");
        }
        this.items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        validate();
    }

    private void validate() {
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderTotalException();
        }
    }
}
