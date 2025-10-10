package demo.ecommerce.entities.exceptions;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException() {
        super("Insufficient stock to complete operation");
    }
}