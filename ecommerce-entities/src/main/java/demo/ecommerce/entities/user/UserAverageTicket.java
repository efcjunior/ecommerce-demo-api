package demo.ecommerce.entities.user;

import demo.ecommerce.entities.exceptions.InvalidAverageTicketException;

import java.math.BigDecimal;

public record UserAverageTicket(String userId, BigDecimal averageTicket) {
    public UserAverageTicket {
        if (averageTicket == null || averageTicket.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidAverageTicketException();
    }
}