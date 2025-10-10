package demo.ecommerce.entities.exceptions;

public class InvalidTotalSpentException extends DomainException {
    public InvalidTotalSpentException() {
        super("Total spent must be non-negative");
    }
}