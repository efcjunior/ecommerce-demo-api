package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.Order;
import demo.ecommerce.entity.OrderItem;
import demo.ecommerce.entity.OrderStatus;
import demo.ecommerce.infrastructure.converter.UUIDAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @Convert(converter = UUIDAttributeConverter.class)
    private UUID id;

    @Column(name = "user_id")
    @Convert(converter = UUIDAttributeConverter.class)
    private UUID userId;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    public static OrderEntity fromDomain(Order order) {
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();

        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> OrderItemEntity.fromDomain(item, entity))
                .collect(Collectors.toList());
        entity.setItems(itemEntities);

        return entity;
    }

    public Order toDomain() {
        List<OrderItem> domainItems = items.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());

        return new Order(id, userId, domainItems, totalAmount, status, createdAt);
    }
}