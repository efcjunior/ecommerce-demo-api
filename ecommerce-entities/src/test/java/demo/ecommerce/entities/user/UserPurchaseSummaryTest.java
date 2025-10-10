package demo.ecommerce.entities.user;

import demo.ecommerce.entities.exceptions.InvalidTotalSpentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserPurchaseSummaryTest {

    @Test
    void shouldCreateWhenTotalSpentIsPositive() {
        UserPurchaseSummary ups = new UserPurchaseSummary("user-123", new BigDecimal("500.00"));
        assertEquals("user-123", ups.userId());
        assertEquals(new BigDecimal("500.00"), ups.totalSpent());
    }

    @Test
    void shouldCreateWhenTotalSpentIsZero() {
        UserPurchaseSummary ups = new UserPurchaseSummary("user-123", BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, ups.totalSpent());
    }

    @Test
    void shouldAcceptPositiveValuesWithDifferentScales() {
        new UserPurchaseSummary("u", new BigDecimal("10"));
        new UserPurchaseSummary("u", new BigDecimal("10.0"));
        new UserPurchaseSummary("u", new BigDecimal("10.00"));
    }

    @Test
    void shouldThrowExceptionWhenTotalSpentIsNull() {
        assertThrows(InvalidTotalSpentException.class,
                () -> new UserPurchaseSummary("user-123", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1", "-100.50"})
    void shouldThrowExceptionWhenTotalSpentIsNegative(String value) {
        assertThrows(InvalidTotalSpentException.class,
                () -> new UserPurchaseSummary("user-123", new BigDecimal(value)));
    }
}