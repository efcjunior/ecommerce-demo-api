package demo.ecommerce.entities.exceptions;

public class InvalidOrderTotalException extends DomainException {
    public InvalidOrderTotalException() {
        super("Total amount must be non-negative");
    }
}