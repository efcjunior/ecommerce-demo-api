package demo.ecommerce.usecases.common;

@FunctionalInterface
public interface VoidUseCase<I> {
    void execute(I input);
}