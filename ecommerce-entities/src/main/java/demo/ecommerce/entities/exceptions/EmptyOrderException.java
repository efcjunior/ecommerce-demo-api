package demo.ecommerce.entities.exceptions;

public class EmptyOrderException extends DomainException {
    public EmptyOrderException() {
        super("Order must have at least one item");
    }
}