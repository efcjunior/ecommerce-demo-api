package demo.ecommerce.entities.user;

import demo.ecommerce.entities.exceptions.InvalidTotalSpentException;

import java.math.BigDecimal;

public record UserPurchaseSummary(String userId, BigDecimal totalSpent) {
    public UserPurchaseSummary {
        if (totalSpent == null || totalSpent.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidTotalSpentException();
    }
}