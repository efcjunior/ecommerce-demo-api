package demo.ecommerce.entities.exceptions;

public class InvalidPriceException extends DomainException {
    public InvalidPriceException() {
        super("Item price must be greater than zero");
    }
}
