package demo.ecommerce.usecases.common;

@FunctionalInterface
public interface UseCase<I, O> {
    O execute(I input);
}
