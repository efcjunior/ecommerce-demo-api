package demo.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {
    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final String category;
    private final int stockQuantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
