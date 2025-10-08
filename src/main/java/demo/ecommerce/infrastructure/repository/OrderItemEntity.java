package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.OrderItem;
import demo.ecommerce.infrastructure.converter.UUIDAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Convert(converter = UUIDAttributeConverter.class)
    private UUID id;

    @Column(name = "product_id")
    @Convert(converter = UUIDAttributeConverter.class)
    private UUID productId;
    private int quantity;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public static OrderItemEntity fromDomain(OrderItem item, OrderEntity order) {
        return OrderItemEntity.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .order(order)
                .build();
    }

    public OrderItem toDomain() {
        return new OrderItem(productId, quantity, price);
    }
}