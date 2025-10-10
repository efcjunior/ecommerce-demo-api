package demo.ecommerce.entities.exceptions;

public class InvalidQuantityException extends DomainException {
    public InvalidQuantityException() {
        super("Item quantity must be positive");
    }
}