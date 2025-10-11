package demo.ecommerce.usecases.common.exceptions;


/**
 * Application exception for entities not found.
 * (Not domain-specific — used only in the use case layer)
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
