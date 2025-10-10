package demo.ecommerce.entities.exceptions;

public class InvalidAverageTicketException extends DomainException {
    public InvalidAverageTicketException() {
        super("Average ticket must be non-negative");
    }
}