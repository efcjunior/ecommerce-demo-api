package demo.ecommerce.usecases.product.command.deleteproduct;

import demo.ecommerce.usecases.common.UseCase;
import demo.ecommerce.usecases.common.exceptions.NotFoundException;
import demo.ecommerce.usecases.ports.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProductInteractor implements UseCase<DeleteProductInput, DeleteProductOutput> {

    private final ProductRepository repository;

    @Override
    public DeleteProductOutput execute(DeleteProductInput input) {
        var existing = repository.findById(input.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        repository.delete(existing.getId());
        return new DeleteProductOutput(true);
    }
}
