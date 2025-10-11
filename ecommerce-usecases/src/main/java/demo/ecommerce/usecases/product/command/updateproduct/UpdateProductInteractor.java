package demo.ecommerce.usecases.product.command.updateproduct;

import demo.ecommerce.entities.product.Product;
import demo.ecommerce.usecases.common.UseCase;
import demo.ecommerce.usecases.common.exceptions.NotFoundException;
import demo.ecommerce.usecases.ports.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateProductInteractor implements UseCase<UpdateProductInput, UpdateProductOutput> {

    private final ProductRepository repository;

    @Override
    public UpdateProductOutput execute(UpdateProductInput input) {
        Product existing = repository.findById(input.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        existing.update(
                input.name(),
                input.description(),
                input.price(),
                input.category(),
                input.stockQuantity()
        );

        Product saved = repository.save(existing);

        return new UpdateProductOutput(saved.getId(), saved.getUpdatedAt());
    }
}
