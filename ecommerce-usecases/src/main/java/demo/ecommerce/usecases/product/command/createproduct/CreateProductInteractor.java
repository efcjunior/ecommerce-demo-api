package demo.ecommerce.usecases.product.command.createproduct;

import demo.ecommerce.entities.product.Product;
import demo.ecommerce.usecases.common.UseCase;
import demo.ecommerce.usecases.ports.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateProductInteractor implements UseCase<CreateProductInput, CreateProductOutput> {

    private final ProductRepository productRepository;

    @Override
    public CreateProductOutput execute(CreateProductInput input) {
        Product product = Product.create(
                input.name(),
                input.description(),
                input.price(),
                input.category(),
                input.stockQuantity()
        );

        Product saved = productRepository.save(product);

        return new CreateProductOutput(saved.getId(), saved.getCreatedAt());
    }
}
