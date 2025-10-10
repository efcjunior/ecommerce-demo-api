package demo.ecommerce.entities.user;


import demo.ecommerce.entities.exceptions.InvalidAverageTicketException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserAverageTicketTest {

    @Test
    void shouldCreateWhenAverageTicketIsPositive() {
        UserAverageTicket uat = new UserAverageTicket("user-123", new BigDecimal("123.45"));
        assertEquals("user-123", uat.userId());
        assertEquals(new BigDecimal("123.45"), uat.averageTicket());
    }

    @Test
    void shouldCreateWhenAverageTicketIsZero() {
        UserAverageTicket uat = new UserAverageTicket("user-123", BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, uat.averageTicket());
    }

    @Test
    void shouldAcceptPositiveValuesWithDifferentScales() {
        new UserAverageTicket("u", new BigDecimal("10"));
        new UserAverageTicket("u", new BigDecimal("10.0"));
        new UserAverageTicket("u", new BigDecimal("10.00"));
    }

    @Test
    void shouldThrowExceptionWhenAverageTicketIsNull() {
        assertThrows(InvalidAverageTicketException.class,
                () -> new UserAverageTicket("user-123", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1", "-100.50"})
    void shouldThrowExceptionWhenAverageTicketIsNegative(String value) {
        assertThrows(InvalidAverageTicketException.class,
                () -> new UserAverageTicket("user-123", new BigDecimal(value)));
    }
}