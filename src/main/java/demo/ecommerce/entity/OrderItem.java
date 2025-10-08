package demo.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderItem {
    private final UUID productId;
    private final int quantity;
    private final BigDecimal price;
}
