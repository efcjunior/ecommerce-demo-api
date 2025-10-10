    package demo.ecommerce.entity;

    import lombok.AllArgsConstructor;
    import lombok.EqualsAndHashCode;
    import lombok.Getter;
    import lombok.ToString;

    import java.math.BigDecimal;

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public class UserPurchaseSummary {

        private final String userId;
        private final BigDecimal totalSpent;
    }
